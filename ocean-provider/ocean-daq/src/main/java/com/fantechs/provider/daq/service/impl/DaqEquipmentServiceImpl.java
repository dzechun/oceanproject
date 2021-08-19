package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentMapper;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentMapper;
import com.fantechs.provider.daq.service.DaqEquipmentService;
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
public class DaqEquipmentServiceImpl extends BaseService<DaqEquipment> implements DaqEquipmentService {

    @Resource
    private DaqEquipmentMapper daqEquipmentMapper;
    @Resource
    private DaqHtEquipmentMapper daqHtEquipmentMapper;

    @Override
    public List<DaqEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = getUser();
            map.put("orgId", user.getOrganizationId());
        }
        return daqEquipmentMapper.findList(map);
    }

    @Override
    public List<DaqHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<DaqEquipment> list) {
        return daqEquipmentMapper.batchUpdate(list);
    }

    @Override
    public DaqEquipment detailByIp(String ip) {
        Example example = new Example(DaqEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        DaqEquipment daqEquipment = daqEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(daqEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        return daqEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(DaqEquipment record) {
        SysUser user = getUser();
        check(record);
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        daqEquipmentMapper.insertUseGeneratedKeys(record);

        DaqHtEquipment daqHtEquipment = new DaqHtEquipment();
        BeanUtils.copyProperties(record, daqHtEquipment);
        int i = daqHtEquipmentMapper.insertSelective(daqHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(DaqEquipment entity) {
        SysUser user = getUser();
        check(entity);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = daqEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        DaqHtEquipment daqHtEquipment = new DaqHtEquipment();
        BeanUtils.copyProperties(entity, daqHtEquipment);
        daqHtEquipmentMapper.insertSelective(daqHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        getUser();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            DaqEquipment daqEquipment = daqEquipmentMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(daqEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            DaqHtEquipment daqHtEquipment = new DaqHtEquipment();
            BeanUtils.copyProperties(daqEquipment, daqHtEquipment);
        }

        return daqEquipmentMapper.deleteByIds(ids);
    }

    public void check(DaqEquipment entity){
        getUser();
        Example example = new Example(DaqEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        DaqEquipment daqEquipment = daqEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(daqEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentIp())) {
            Example examples = new Example(DaqEquipment.class);
            Example.Criteria criterias = examples.createCriteria();
            criterias.andEqualTo("equipmentIp", entity.getEquipmentIp());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                criterias.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(daqEquipmentMapper.selectOneByExample(examples))) {
                throw new BizErrorException("设备ip不能重复");
            }
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentMacAddress())){
            Example macExample = new Example(DaqEquipment.class);
            Example.Criteria macCriteria = macExample.createCriteria();
            macCriteria.andEqualTo("equipmentMacAddress", entity.getEquipmentMacAddress());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                macCriteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(daqEquipmentMapper.selectOneByExample(macExample))) {
                throw new BizErrorException("设备mac地址不能重复");
            }
        }
    }


    @Override
    public List<DaqEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentMapper.findNoGroup(map);
    }



    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

}
