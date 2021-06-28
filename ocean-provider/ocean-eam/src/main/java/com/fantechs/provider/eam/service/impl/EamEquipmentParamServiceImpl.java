package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentParamMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentParamMapper;
import com.fantechs.provider.eam.service.EamEquipmentParamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentParamServiceImpl extends BaseService<EamEquipmentParam> implements EamEquipmentParamService {

    @Resource
    private EamEquipmentParamMapper eamEquipmentParamMapper;
    @Resource
    private EamHtEquipmentParamMapper eamHtEquipmentParamMapper;

    @Override
    public List<EamEquipmentParamDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentParamMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentParam record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        EamEquipmentParam eamEquipmentParam = eamEquipmentParamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentParam)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = eamEquipmentParamMapper.insertUseGeneratedKeys(record);

        EamHtEquipmentParam eamHtEquipmentParam = new EamHtEquipmentParam();
        BeanUtils.copyProperties(record, eamHtEquipmentParam);
        eamHtEquipmentParamMapper.insert(eamHtEquipmentParam);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentParam entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId())
                .andNotEqualTo("equipmentParamId",entity.getEquipmentParamId());
        EamEquipmentParam eamEquipmentParam = eamEquipmentParamMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentParam)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtEquipmentParam eamHtEquipmentParam = new EamHtEquipmentParam();
        BeanUtils.copyProperties(entity, eamHtEquipmentParam);
        eamHtEquipmentParamMapper.insert(eamHtEquipmentParam);

        return eamEquipmentParamMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        return super.batchDelete(ids);
    }
}
