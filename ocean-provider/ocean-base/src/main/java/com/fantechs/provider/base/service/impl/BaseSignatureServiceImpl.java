package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseSignatureImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSignatureMapper;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.mapper.BaseSignatureMapper;
import com.fantechs.provider.base.mapper.BaseSupplierMapper;
import com.fantechs.provider.base.service.BaseSignatureService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@Service
public class BaseSignatureServiceImpl extends BaseService<BaseSignature> implements BaseSignatureService {

    @Resource
    private BaseSignatureMapper baseSignatureMapper;
    @Resource
    private BaseHtSignatureMapper baseHtSignatureMapper;
    @Resource
    private BaseSupplierMapper baseSupplierMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseSignature baseSignature) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("signatureCode",baseSignature.getSignatureCode());
        List<BaseSignature> baseSignatures = baseSignatureMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseSignatures)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //特征码转正则表达式
        String signatureCode = baseSignature.getSignatureCode();
        if(!this.checkParam(signatureCode)){
            throw new BizErrorException("输入的特征码不符合要求");
        }
        baseSignature.setSignatureRegex(this.convertRegex(signatureCode));

        baseSignature.setCreateUserId(currentUser.getUserId());
        baseSignature.setCreateTime(new Date());
        baseSignature.setModifiedUserId(currentUser.getUserId());
        baseSignature.setModifiedTime(new Date());
        baseSignature.setOrganizationId(currentUser.getOrganizationId());
        baseSignatureMapper.insertUseGeneratedKeys(baseSignature);

        //新增物料特征码历史信息
        BaseHtSignature baseHtSignature=new BaseHtSignature();
        BeanUtils.copyProperties(baseSignature,baseHtSignature);
        int i = baseHtSignatureMapper.insertSelective(baseHtSignature);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<BaseHtSignature> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] signatureIds = ids.split(",");
        for (String signatureId : signatureIds) {
            BaseSignature baseSignature = baseSignatureMapper.selectByPrimaryKey(Long.parseLong(signatureId));
            if(StringUtils.isEmpty(baseSignature)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增物料特征码历史信息
            BaseHtSignature baseHtSignature=new BaseHtSignature();
            BeanUtils.copyProperties(baseSignature,baseHtSignature);
            baseHtSignature.setModifiedUserId(currentUser.getUserId());
            baseHtSignature.setModifiedTime(new Date());
            list.add(baseHtSignature);
        }
        baseHtSignatureMapper.insertList(list);

        return baseSignatureMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseSignature baseSignature) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("signatureCode",baseSignature.getSignatureCode());

        BaseSignature signature = baseSignatureMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(signature)&&!signature.getSignatureId().equals(baseSignature.getSignatureId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //特征码转正则表达式
        String signatureCode = baseSignature.getSignatureCode();
        if(!this.checkParam(signatureCode)){
            throw new BizErrorException("输入的特征码不符合要求");
        }
        baseSignature.setSignatureRegex(this.convertRegex(signatureCode));

        baseSignature.setModifiedUserId(currentUser.getUserId());
        baseSignature.setModifiedTime(new Date());
        baseSignature.setOrganizationId(currentUser.getOrganizationId());
        int i= baseSignatureMapper.updateByPrimaryKeySelective(baseSignature);

        //新增物料特征码历史信息
        BaseHtSignature baseHtSignature=new BaseHtSignature();
        BeanUtils.copyProperties(baseSignature,baseHtSignature);
        baseHtSignatureMapper.insertSelective(baseHtSignature);
        return i;
    }

    @Override
    public List<BaseSignature> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSignatureMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSignatureImport> baseSignatureImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseSignature> list = new LinkedList<>();
        LinkedList<BaseHtSignature> htList = new LinkedList<>();
        LinkedList<BaseSignatureImport> signatureImports = new LinkedList<>();
        for (int i = 0; i < baseSignatureImports.size(); i++) {
            BaseSignatureImport baseSignatureImport = baseSignatureImports.get(i);
            String materialCode = baseSignatureImport.getMaterialCode();
            String signatureCode = baseSignatureImport.getSignatureCode();
            String supplierCode = baseSignatureImport.getSupplierCode();
            if (StringUtils.isEmpty(
                    materialCode,signatureCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            Example example1 = new Example(BaseMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            baseSignatureImport.setMaterialId(baseMaterial.getMaterialId());

            //判断物料和物料的特征码是否已经存在
            Example example = new Example(BaseSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("materialId",baseMaterial.getMaterialId())
                    .orEqualTo("signatureCode",signatureCode);
            if (StringUtils.isNotEmpty(baseSignatureMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //供应商编码不为空则判断供应商信息是否存在
            if (StringUtils.isNotEmpty(supplierCode)){
                Example example2 = new Example(BaseSupplier.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria2.andEqualTo("supplierCode", supplierCode);
                BaseSupplier baseSupplier = baseSupplierMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseSupplier)){
                    fail.add(i+4);
                    continue;
                }
                baseSignatureImport.setSupplierId(baseSupplier.getSupplierId());
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(signatureImports)){
                for (BaseSignatureImport signatureImport : signatureImports) {
                    if (signatureImport.getMaterialCode().equals(materialCode) || signatureImport.getSignatureCode().equals(signatureCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            signatureImports.add(baseSignatureImport);
        }

        if (StringUtils.isNotEmpty(signatureImports)){
            for (BaseSignatureImport signatureImport : signatureImports) {
                BaseSignature baseSignature = new BaseSignature();
                BeanUtils.copyProperties(signatureImport,baseSignature);
                baseSignature.setCreateTime(new Date());
                baseSignature.setCreateUserId(currentUser.getUserId());
                baseSignature.setModifiedTime(new Date());
                baseSignature.setModifiedUserId(currentUser.getUserId());
                if (StringUtils.isEmpty(baseSignature.getStatus())){
                    baseSignature.setStatus(1);
                }
                baseSignature.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseSignature);
            }

            success = baseSignatureMapper.insertList(list);

            for (BaseSignature baseSignature : list) {
                BaseHtSignature baseHtSignature = new BaseHtSignature();
                BeanUtils.copyProperties(baseSignature,baseHtSignature);
                htList.add(baseHtSignature);
            }
            baseHtSignatureMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    public boolean checkParam(String str){
        boolean bool = false;
        //只允许字母、数字、#、*
        String pattern = "^[a-zA-Z0-9*#]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(str);
        if(matcher.find()){
            bool = true;
        }

        return bool;
    }

    public String convertRegex(String signatureCode){
        String s1 = signatureCode.replaceAll("[*]", "[a-zA-Z0-9]*");
        String s2 = s1.replaceAll("[#]", "[a-zA-Z0-9]");
        String signatureRegex = "^"+s2+"$";
        return signatureRegex;
    }
}
