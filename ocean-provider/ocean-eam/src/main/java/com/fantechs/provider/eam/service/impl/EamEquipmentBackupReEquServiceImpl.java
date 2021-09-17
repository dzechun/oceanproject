package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupReEquDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackupReEqu;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackupReEqu;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentBackupReEquMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentBackupReEquMapper;
import com.fantechs.provider.eam.service.EamEquipmentBackupReEquService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */
@Service
public class EamEquipmentBackupReEquServiceImpl extends BaseService<EamEquipmentBackupReEqu> implements EamEquipmentBackupReEquService {

    @Resource
    private EamEquipmentBackupReEquMapper eamEquipmentBackupReEquMapper;
    @Resource
    private EamHtEquipmentBackupReEquMapper eamHtEquipmentBackupReEquMapper;

    @Override
    public List<EamEquipmentBackupReEquDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentBackupReEquMapper.findList(map);
    }

    @Override
    public int save(EamEquipmentBackupReEqu record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentBackupReEqu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", record.getEquipmentId());
        criteria.andEqualTo("equipmentBackupId", record.getEquipmentBackupId());
        EamEquipmentBackupReEqu eamEquipmentBackupReEqu = eamEquipmentBackupReEquMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentBackupReEqu)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentBackupReEquMapper.insertUseGeneratedKeys(record);

        //履历
        EamHtEquipmentBackupReEqu eamHtEquipmentBackupReEqu = new EamHtEquipmentBackupReEqu();
        BeanUtils.copyProperties(record, eamHtEquipmentBackupReEqu);
        int i = eamHtEquipmentBackupReEquMapper.insert(eamHtEquipmentBackupReEqu);
        return i;
    }

    @Override
    public int update(EamEquipmentBackupReEqu entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentBackupReEqu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentId", entity.getEquipmentId());
        criteria.andEqualTo("equipmentBackupId", entity.getEquipmentBackupId());
        EamEquipmentBackupReEqu eamEquipmentBackupReEqu = eamEquipmentBackupReEquMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentBackupReEqu)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentBackupReEquMapper.updateByPrimaryKeySelective(entity);

        //履历
        EamHtEquipmentBackupReEqu eamHtEquipmentBackupReEqu = new EamHtEquipmentBackupReEqu();
        BeanUtils.copyProperties(entity, eamHtEquipmentBackupReEqu);
        int i = eamHtEquipmentBackupReEquMapper.insert(eamHtEquipmentBackupReEqu);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<EamHtEquipmentBackupReEqu> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentBackupReEqu eamEquipmentBackupReEqu = eamEquipmentBackupReEquMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentBackupReEqu)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtEquipmentBackupReEqu eamHtEquipmentBackupReEqu = new EamHtEquipmentBackupReEqu();
            BeanUtils.copyProperties(eamEquipmentBackupReEqu, eamHtEquipmentBackupReEqu);
            list.add(eamHtEquipmentBackupReEqu);
        }
        //履历
        eamHtEquipmentBackupReEquMapper.insertList(list);

        return eamEquipmentBackupReEquMapper.deleteByIds(ids);
    }
}
