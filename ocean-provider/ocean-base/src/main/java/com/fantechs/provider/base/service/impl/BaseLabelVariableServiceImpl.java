package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelVariableDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelVariableImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseShipmentEnterpriseImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelVariable;
import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtLabelVariableMapper;
import com.fantechs.provider.base.mapper.BaseLabelCategoryMapper;
import com.fantechs.provider.base.mapper.BaseLabelVariableMapper;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.service.BaseLabelVariableService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */
@Service
public class BaseLabelVariableServiceImpl extends BaseService<BaseLabelVariable> implements BaseLabelVariableService {

    @Resource
    private BaseLabelVariableMapper baseLabelVariableMapper;
    @Resource
    private BaseHtLabelVariableMapper baseHtLabelVariableMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseLabelCategoryMapper baseLabelCategoryMapper;

    @Override
    public List<BaseLabelVariableDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseLabelVariableMapper.findList(map);
    }

    @Override
    public List<BaseHtLabelVariable> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtLabelVariableMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseLabelVariable record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        baseLabelVariableMapper.insertUseGeneratedKeys(record);

        BaseHtLabelVariable baseHtLabelVariable = new BaseHtLabelVariable();
        BeanUtils.copyProperties(record,baseHtLabelVariable);
        int i = baseHtLabelVariableMapper.insertSelective(baseHtLabelVariable);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseLabelVariable entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());

        BaseHtLabelVariable baseHtLabelVariable = new BaseHtLabelVariable();
        BeanUtils.copyProperties(entity,baseHtLabelVariable);
        baseHtLabelVariableMapper.insertSelective(baseHtLabelVariable);

        return baseLabelVariableMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtLabelVariable> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseLabelVariable baseLabelVariable = baseLabelVariableMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseLabelVariable)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtLabelVariable baseHtLabelVariable = new BaseHtLabelVariable();
            BeanUtils.copyProperties(baseLabelVariable,baseHtLabelVariable);
            list.add(baseHtLabelVariable);
        }

        baseHtLabelVariableMapper.insertList(list);
        return baseLabelVariableMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseLabelVariableImport> baseLabelVariableImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseLabelVariable> list = new LinkedList<>();
        LinkedList<BaseHtLabelVariable> htList = new LinkedList<>();
        for (int i = 0; i < baseLabelVariableImports.size(); i++) {
            BaseLabelVariableImport baseLabelVariableImport = baseLabelVariableImports.get(i);

            String labelCategoryCode = baseLabelVariableImport.getLabelCategoryCode();
            String materialCode = baseLabelVariableImport.getMaterialCode();
            if (StringUtils.isEmpty(
                    labelCategoryCode,materialCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断物料编码是否存在
            Example example = new Example(BaseMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            baseLabelVariableImport.setMaterialId(baseMaterial.getMaterialId());

            //判断标签类别编码是否存在
            Example example1 = new Example(BaseLabelCategory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria1.andEqualTo("labelCategoryCode",labelCategoryCode);
            BaseLabelCategory baseLabelCategory = baseLabelCategoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseLabelCategory)){
                fail.add(i+4);
                continue;
            }
            baseLabelVariableImport.setLabelCategoryId(baseLabelCategory.getLabelCategoryId());

            BaseLabelVariable baseLabelVariable = new BaseLabelVariable();
            BeanUtils.copyProperties(baseLabelVariableImport, baseLabelVariable);
            baseLabelVariable.setCreateTime(new Date());
            baseLabelVariable.setCreateUserId(currentUser.getUserId());
            baseLabelVariable.setModifiedTime(new Date());
            baseLabelVariable.setModifiedUserId(currentUser.getUserId());
            baseLabelVariable.setStatus((byte)1);
            baseLabelVariable.setOrgId(currentUser.getOrganizationId());
            list.add(baseLabelVariable);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseLabelVariableMapper.insertList(list);
        }

        for (BaseLabelVariable baseLabelVariable : list) {
            BaseHtLabelVariable baseHtLabelVariable = new BaseHtLabelVariable();
            BeanUtils.copyProperties(baseLabelVariable, baseHtLabelVariable);
            htList.add(baseHtLabelVariable);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtLabelVariableMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
