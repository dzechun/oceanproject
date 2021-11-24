package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentCategoryMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentCategoryMapper;
import com.fantechs.provider.eam.service.EamEquipmentCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentCategoryServiceImpl extends BaseService<EamEquipmentCategory> implements EamEquipmentCategoryService {

    @Resource
    private EamEquipmentCategoryMapper eamEquipmentCategoryMapper;
    @Resource
    private EamHtEquipmentCategoryMapper eamHtEquipmentCategoryMapper;

    @Override
    public List<EamEquipmentCategoryDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentCategoryMapper.findList(map);
    }

    @Override
    public List<EamHtEquipmentCategory> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentCategoryMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentCategory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.ifRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentCategoryMapper.insertUseGeneratedKeys(record);

        EamHtEquipmentCategory eamHtEquipmentCategory = new EamHtEquipmentCategory();
        BeanUtils.copyProperties(record, eamHtEquipmentCategory);
        int i = eamHtEquipmentCategoryMapper.insertSelective(eamHtEquipmentCategory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentCategory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.ifRepeat(entity);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtEquipmentCategory eamHtEquipmentCategory = new EamHtEquipmentCategory();
        BeanUtils.copyProperties(entity, eamHtEquipmentCategory);
        eamHtEquipmentCategoryMapper.insertSelective(eamHtEquipmentCategory);

        return eamEquipmentCategoryMapper.updateByPrimaryKeySelective(entity);
    }

    public void ifRepeat(EamEquipmentCategory eamEquipmentCategory){
        Example example = new Example(EamEquipmentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCategoryCode", eamEquipmentCategory.getEquipmentCategoryCode());
        if(StringUtils.isNotEmpty(eamEquipmentCategory.getEquipmentCategoryId())){
            criteria.andNotEqualTo("equipmentCategoryId",eamEquipmentCategory.getEquipmentCategoryId());
        }
        EamEquipmentCategory equipmentCategory = eamEquipmentCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equipmentCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("equipmentCategoryName", eamEquipmentCategory.getEquipmentCategoryName());
        if(StringUtils.isNotEmpty(eamEquipmentCategory.getEquipmentCategoryId())){
            criteria1.andNotEqualTo("equipmentCategoryId",eamEquipmentCategory.getEquipmentCategoryId());
        }
        EamEquipmentCategory equipmentCategory1 = eamEquipmentCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(equipmentCategory1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"名称重复");
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtEquipmentCategory> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentCategory eamEquipmentCategory = eamEquipmentCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentCategory eamHtEquipmentCategory = new EamHtEquipmentCategory();
            BeanUtils.copyProperties(eamEquipmentCategory, eamHtEquipmentCategory);
            list.add(eamHtEquipmentCategory);
        }

        eamHtEquipmentCategoryMapper.insertList(list);

        return eamEquipmentCategoryMapper.deleteByIds(ids);
    }
}
