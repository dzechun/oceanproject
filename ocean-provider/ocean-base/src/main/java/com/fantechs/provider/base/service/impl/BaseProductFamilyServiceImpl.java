package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductFamilyImport;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductFamilyMapper;
import com.fantechs.provider.base.mapper.BaseProductFamilyMapper;
import com.fantechs.provider.base.service.BaseProductFamilyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/15.
 */
@Service
public class BaseProductFamilyServiceImpl extends BaseService<BaseProductFamily> implements BaseProductFamilyService {

    @Resource
    private BaseProductFamilyMapper baseProductFamilyMapper;
    @Resource
    private BaseHtProductFamilyMapper baseHtProductFamilyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductFamily baseProductFamily) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductFamily.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("productFamilyCode",baseProductFamily.getProductFamilyCode());
        BaseProductFamily baseProductFamily1 = baseProductFamilyMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductFamily1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        Example example1 = new Example(BaseProductFamily.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("productFamilyName",baseProductFamily.getProductFamilyName());
        BaseProductFamily baseProductFamily2 = baseProductFamilyMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseProductFamily2)){
            throw new BizErrorException("产品族名称已存在");
        }

        baseProductFamily.setCreateTime(new Date());
        baseProductFamily.setCreateUserId(user.getUserId());
        baseProductFamily.setModifiedTime(new Date());
        baseProductFamily.setModifiedUserId(user.getUserId());
        baseProductFamily.setStatus(StringUtils.isEmpty(baseProductFamily.getStatus())?1:baseProductFamily.getStatus());
        baseProductFamily.setOrganizationId(user.getOrganizationId());
        int i = baseProductFamilyMapper.insertUseGeneratedKeys(baseProductFamily);

        BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
        BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
        baseHtProductFamilyMapper.insertSelective(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductFamily baseProductFamily) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductFamily.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("productFamilyCode",baseProductFamily.getProductFamilyCode())
                .andNotEqualTo("productFamilyId",baseProductFamily.getProductFamilyId());
        BaseProductFamily baseProductFamily1 = baseProductFamilyMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductFamily1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseProductFamily.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("productFamilyName",baseProductFamily.getProductFamilyName())
                .andNotEqualTo("productFamilyId",baseProductFamily.getProductFamilyId());
        BaseProductFamily baseProductFamily2 = baseProductFamilyMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseProductFamily2)){
            throw new BizErrorException("产品族名称已经存在");
        }

        baseProductFamily.setModifiedUserId(user.getUserId());
        baseProductFamily.setModifiedTime(new Date());
        baseProductFamily.setOrganizationId(user.getOrganizationId());

        BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
        BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
        baseHtProductFamilyMapper.insertSelective(baseHtProductFamily);

        return baseProductFamilyMapper.updateByPrimaryKeySelective(baseProductFamily);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtProductFamily> baseHtProductFamilies = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseProductFamily baseProductFamily = baseProductFamilyMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseProductFamily)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
            BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
            baseHtProductFamily.setModifiedTime(new Date());
            baseHtProductFamily.setModifiedUserId(user.getUserId());
            baseHtProductFamilies.add(baseHtProductFamily);
        }

        baseHtProductFamilyMapper.insertList(baseHtProductFamilies);
        return baseProductFamilyMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseProductFamilyDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseProductFamilyMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseProductFamilyImport> baseProductFamilyImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseProductFamily> list = new LinkedList<>();
        LinkedList<BaseHtProductFamily> htList = new LinkedList<>();
        LinkedList<BaseProductFamilyImport> productFamilyImports = new LinkedList<>();

        for (int i = 0; i < baseProductFamilyImports.size(); i++) {
            BaseProductFamilyImport baseProductFamilyImport = baseProductFamilyImports.get(i);
            String productFamilyCode = baseProductFamilyImport.getProductFamilyCode();
            String productFamilyName = baseProductFamilyImport.getProductFamilyName();
            if (StringUtils.isEmpty(
                    productFamilyCode,productFamilyName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseProductFamily.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("productFamilyCode",productFamilyCode);
            if (StringUtils.isNotEmpty(baseProductFamilyMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断结合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(productFamilyImports)){
                for (BaseProductFamilyImport productFamilyImport : productFamilyImports) {
                    if (productFamilyImport.getProductFamilyCode().equals(productFamilyCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }
            productFamilyImports.add(baseProductFamilyImport);
        }

        if (StringUtils.isNotEmpty(productFamilyImports)){
            for (BaseProductFamilyImport productFamilyImport : productFamilyImports) {
                BaseProductFamily baseProductFamily = new BaseProductFamily();
                BeanUtils.copyProperties(productFamilyImport,baseProductFamily);
                baseProductFamily.setCreateTime(new Date());
                baseProductFamily.setCreateUserId(currentUser.getUserId());
                baseProductFamily.setModifiedTime(new Date());
                baseProductFamily.setModifiedUserId(currentUser.getUserId());
                baseProductFamily.setStatus((byte) 1);
                baseProductFamily.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseProductFamily);
            }

            success = baseProductFamilyMapper.insertList(list);

            for (BaseProductFamily baseProductFamily : list) {
                BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
                BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
                htList.add(baseHtProductFamily);
            }
            if (StringUtils.isNotEmpty(htList)){
                baseHtProductFamilyMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
