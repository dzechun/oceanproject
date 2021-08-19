package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMapper;
import com.fantechs.provider.eam.service.EamEquipmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentServiceImpl extends BaseService<EamEquipment> implements EamEquipmentService {

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private EamHtEquipmentMapper eamHtEquipmentMapper;

    @Override
    public List<EamEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = getUser();
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentMapper.findList(map);
    }

    @Override
    public List<EamHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<EamEquipment> list) {
        return eamEquipmentMapper.batchUpdate(list);
    }

    @Override
    public EamEquipment detailByIp(String ip) {
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(eamEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        return eamEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipment record) {
        SysUser user = getUser();
        record.setOrgId(user.getOrganizationId());
        check(record);
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamEquipmentMapper.insertUseGeneratedKeys(record);

        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(record, eamHtEquipment);
        int i = eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipment entity) {
        SysUser user = getUser();
        check(entity);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(entity, eamHtEquipment);
        eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        getUser();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipment eamHtEquipment = new EamHtEquipment();
            BeanUtils.copyProperties(eamEquipment, eamHtEquipment);
        }

        return eamEquipmentMapper.deleteByIds(ids);
    }

    public void check(EamEquipment entity){
        getUser();
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentIp())) {
            Example examples = new Example(EamEquipment.class);
            Example.Criteria criterias = examples.createCriteria();
            criterias.andEqualTo("equipmentIp", entity.getEquipmentIp());
            criterias.andEqualTo("orgId", entity.getOrgId());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                criterias.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(eamEquipmentMapper.selectOneByExample(examples))) {
                throw new BizErrorException("设备ip不能重复");
            }
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentSeqNum())) {
            Example numExample = new Example(EamEquipment.class);
            Example.Criteria numCriteria = numExample.createCriteria();
            numCriteria.andEqualTo("equipmentSeqNum", entity.getEquipmentSeqNum());
            numCriteria.andEqualTo("proLineId", entity.getProLineId());
            numCriteria.andEqualTo("orgId", entity.getOrgId());
            if (StringUtils.isNotEmpty(eamEquipmentMapper.selectOneByExample(numExample))) {
                throw new BizErrorException("同一产线的设备编码不能重复");
            }
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentMacAddress())){
            Example macExample = new Example(EamEquipment.class);
            Example.Criteria macCriteria = macExample.createCriteria();
            macCriteria.andEqualTo("equipmentMacAddress", entity.getEquipmentMacAddress());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                macCriteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(eamEquipmentMapper.selectOneByExample(macExample))) {
                throw new BizErrorException("设备mac地址不能重复");
            }
        }
    }


    @Override
    public List<EamEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findNoGroup(map);
    }



    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

}
