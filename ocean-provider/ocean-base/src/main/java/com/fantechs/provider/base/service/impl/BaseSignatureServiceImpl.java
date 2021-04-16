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
import javax.validation.constraints.NotBlank;
import java.util.*;

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
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("signatureCode",baseSignature.getSignatureCode());
        List<BaseSignature> baseSignatures = baseSignatureMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseSignatures)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

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
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseSignature.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("signatureCode",baseSignature.getSignatureCode());

        BaseSignature signature = baseSignatureMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(signature)&&!signature.getSignatureId().equals(baseSignature.getSignatureId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

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
    public List<BaseSignature> findList(SearchBaseSignature searchBaseSignature) {
        return baseSignatureMapper.findList(searchBaseSignature);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSignatureImport> baseSignatureImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
}
