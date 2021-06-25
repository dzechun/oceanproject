package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
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
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentCategoryMapper.findList(map);
    }

    @Override
    public List<EamHtEquipmentCategory> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentCategoryMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentCategory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCategoryCode", record.getEquipmentCategoryCode());
        EamEquipmentCategory eamEquipmentCategory = eamEquipmentCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = eamEquipmentCategoryMapper.insertUseGeneratedKeys(record);

        EamHtEquipmentCategory eamHtEquipmentCategory = new EamHtEquipmentCategory();
        BeanUtils.copyProperties(record, eamHtEquipmentCategory);
        eamHtEquipmentCategoryMapper.insert(eamHtEquipmentCategory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentCategory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCategoryCode", entity.getEquipmentCategoryCode())
                .andNotEqualTo("equipmentCategoryId",entity.getEquipmentCategoryId());
        EamEquipmentCategory eamEquipmentCategory = eamEquipmentCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtEquipmentCategory eamHtEquipmentCategory = new EamHtEquipmentCategory();
        BeanUtils.copyProperties(entity, eamHtEquipmentCategory);
        eamHtEquipmentCategoryMapper.insert(eamHtEquipmentCategory);

        return eamEquipmentCategoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
