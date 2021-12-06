package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtEquipment;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.EsopEquipmentMapper;
import com.fantechs.provider.esop.mapper.EsopHtEquipmentMapper;
import com.fantechs.provider.esop.service.EsopEquipmentService;
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
public class EsopEquipmentServiceImpl extends BaseService<EsopEquipment> implements EsopEquipmentService {

    @Resource
    private EsopEquipmentMapper esopEquipmentMapper;
    @Resource
    private EsopHtEquipmentMapper esopHtEquipmentMapper;


    @Override
    public List<EsopEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return esopEquipmentMapper.findList(map);
    }

    @Override
    public List<EsopHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return esopHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<EsopEquipment> list) {
        return esopEquipmentMapper.batchUpdate(list);
    }

    @Override
    public EsopEquipment detailByMacAddress(String macAddress) {
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentMacAddress",macAddress);
        EsopEquipment EsopEquipment = esopEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(EsopEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到mac地址对应的设备信息");
        }
        return EsopEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EsopEquipment record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setOrgId(user.getOrganizationId());
        check(record);

        if(StringUtils.isNotEmpty(record.getEquipmentSeqNum())) {
            Example numExample = new Example(EsopEquipment.class);
            Example.Criteria numCriteria = numExample.createCriteria();
            numCriteria.andEqualTo("equipmentSeqNum", record.getEquipmentSeqNum());
            numCriteria.andEqualTo("proLineId", record.getProLineId());
            numCriteria.andEqualTo("orgId", record.getOrgId());
            if (StringUtils.isNotEmpty(esopEquipmentMapper.selectOneByExample(numExample))) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"同一产线的设备编码不能重复");
            }
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        esopEquipmentMapper.insertSelective(record);

        EsopHtEquipment EsopHtEquipment = new EsopHtEquipment();
        BeanUtils.copyProperties(record, EsopHtEquipment);
        int i = esopHtEquipmentMapper.insertSelective(EsopHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EsopEquipment entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        check(entity);
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = esopEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        EsopHtEquipment EsopHtEquipment = new EsopHtEquipment();
        BeanUtils.copyProperties(entity, EsopHtEquipment);
        esopHtEquipmentMapper.insertSelective(EsopHtEquipment);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        return esopEquipmentMapper.deleteByIds(ids);
    }

    public void check(EsopEquipment entity){
        CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        EsopEquipment EsopEquipment = esopEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(EsopEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        if(StringUtils.isNotEmpty(entity.getEquipmentMacAddress())){
            Example macExample = new Example(EsopEquipment.class);
            Example.Criteria macCriteria = macExample.createCriteria();
            macCriteria.andEqualTo("equipmentMacAddress", entity.getEquipmentMacAddress());
            if(StringUtils.isNotEmpty(entity.getEquipmentId())){
                macCriteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
            }
            if (StringUtils.isNotEmpty(esopEquipmentMapper.selectOneByExample(macExample))) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"设备mac地址不能重复");
            }
        }
    }


    @Override
    public List<EsopEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return esopEquipmentMapper.findNoGroup(map);
    }

}
