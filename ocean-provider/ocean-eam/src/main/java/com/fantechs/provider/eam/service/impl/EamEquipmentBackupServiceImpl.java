package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackup;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackup;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentBackupMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentBackupMapper;
import com.fantechs.provider.eam.service.EamEquipmentBackupService;
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
public class EamEquipmentBackupServiceImpl extends BaseService<EamEquipmentBackup> implements EamEquipmentBackupService {

    @Resource
    private EamEquipmentBackupMapper eamEquipmentBackupMapper;
    @Resource
    private EamHtEquipmentBackupMapper eamHtEquipmentBackupMapper;

    @Override
    public List<EamEquipmentBackupDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentBackupMapper.findList(map);
    }

    @Override
    public int save(EamEquipmentBackup record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentBackup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBackupCode", record.getEquipmentBackupCode());
        EamEquipmentBackup eamEquipmentBackup = eamEquipmentBackupMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentBackup)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentBackupMapper.insertUseGeneratedKeys(record);

        //履历
        EamHtEquipmentBackup eamHtEquipmentBackup = new EamHtEquipmentBackup();
        BeanUtils.copyProperties(record, eamHtEquipmentBackup);
        int i = eamHtEquipmentBackupMapper.insert(eamHtEquipmentBackup);
        return i;
    }

    @Override
    public int update(EamEquipmentBackup entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamEquipmentBackup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBackupCode", entity.getEquipmentBackupCode());
        EamEquipmentBackup eamEquipmentBackup = eamEquipmentBackupMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentBackup)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamEquipmentBackupMapper.updateByPrimaryKeySelective(entity);

        //履历
        EamHtEquipmentBackup eamHtEquipmentBackup = new EamHtEquipmentBackup();
        BeanUtils.copyProperties(entity, eamHtEquipmentBackup);
        int i = eamHtEquipmentBackupMapper.insert(eamHtEquipmentBackup);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<EamHtEquipmentBackup> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipmentBackup eamEquipmentBackup = eamEquipmentBackupMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipmentBackup)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentBackup eamHtEquipmentBackup = new EamHtEquipmentBackup();
            BeanUtils.copyProperties(eamEquipmentBackup, eamHtEquipmentBackup);
            list.add(eamHtEquipmentBackup);
        }
        //履历
        eamHtEquipmentBackupMapper.insertList(list);

        return eamEquipmentBackupMapper.deleteByIds(ids);
    }
}
