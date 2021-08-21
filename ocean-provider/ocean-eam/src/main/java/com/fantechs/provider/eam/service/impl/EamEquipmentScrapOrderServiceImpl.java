package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrderDet;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrder;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentScrapOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentScrapOrderDetMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentScrapOrderMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentScrapOrderMapper;
import com.fantechs.provider.eam.service.EamEquipmentScrapOrderService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentScrapOrderServiceImpl extends BaseService<EamEquipmentScrapOrder> implements EamEquipmentScrapOrderService {

    @Resource
    private EamEquipmentScrapOrderMapper eamEquipmentScrapOrderMapper;
    @Resource
    private EamEquipmentScrapOrderDetMapper eamEquipmentScrapOrderDetMapper;
    @Resource
    private EamHtEquipmentScrapOrderMapper eamHtEquipmentScrapOrderMapper;

    @Override
    public List<EamEquipmentScrapOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        
        return eamEquipmentScrapOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentScrapOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        eamEquipmentScrapOrderMapper.insertUseGeneratedKeys(record);

        //报废单明细
        List<EamEquipmentScrapOrderDetDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto : list){
                eamEquipmentScrapOrderDetDto.setEquipmentScrapOrderId(record.getEquipmentScrapOrderId());
                eamEquipmentScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setCreateTime(new Date());
                eamEquipmentScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setModifiedTime(new Date());
                eamEquipmentScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamEquipmentScrapOrderDetDto.getStatus())?1: eamEquipmentScrapOrderDetDto.getStatus());
                eamEquipmentScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentScrapOrderDetMapper.insertList(list);
        }

        EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
        BeanUtils.copyProperties(record,eamHtEquipmentScrapOrder);
        int i = eamHtEquipmentScrapOrderMapper.insert(eamHtEquipmentScrapOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentScrapOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamEquipmentScrapOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原报废单明细
        Example example1 = new Example(EamEquipmentScrapOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentScrapOrderId", entity.getEquipmentScrapOrderId());
        eamEquipmentScrapOrderDetMapper.deleteByExample(example1);

        //报废单明细
        List<EamEquipmentScrapOrderDetDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamEquipmentScrapOrderDetDto eamEquipmentScrapOrderDetDto : list){
                eamEquipmentScrapOrderDetDto.setEquipmentScrapOrderId(entity.getEquipmentScrapOrderId());
                eamEquipmentScrapOrderDetDto.setCreateUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setCreateTime(new Date());
                eamEquipmentScrapOrderDetDto.setModifiedUserId(user.getUserId());
                eamEquipmentScrapOrderDetDto.setModifiedTime(new Date());
                eamEquipmentScrapOrderDetDto.setStatus(StringUtils.isEmpty(eamEquipmentScrapOrderDetDto.getStatus())?1: eamEquipmentScrapOrderDetDto.getStatus());
                eamEquipmentScrapOrderDetDto.setOrgId(user.getOrganizationId());
            }
            eamEquipmentScrapOrderDetMapper.insertList(list);
        }

        EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
        BeanUtils.copyProperties(entity,eamHtEquipmentScrapOrder);
        int i = eamHtEquipmentScrapOrderMapper.insert(eamHtEquipmentScrapOrder);

        return i;
    }

    private void codeIfRepeat(EamEquipmentScrapOrderDto eamEquipmentScrapOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(EamEquipmentScrapOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("equipmentScrapOrderCode",eamEquipmentScrapOrderDto.getEquipmentScrapOrderCode());
        if (StringUtils.isNotEmpty(eamEquipmentScrapOrderDto.getEquipmentScrapOrderId())){
            criteria.andNotEqualTo("equipmentScrapOrderId",eamEquipmentScrapOrderDto.getEquipmentScrapOrderId());
        }

        EamEquipmentScrapOrder eamEquipmentScrapOrder = eamEquipmentScrapOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipmentScrapOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtEquipmentScrapOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamEquipmentScrapOrder eamEquipmentScrapOrder = eamEquipmentScrapOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamEquipmentScrapOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentScrapOrder eamHtEquipmentScrapOrder = new EamHtEquipmentScrapOrder();
            BeanUtils.copyProperties(eamEquipmentScrapOrder,eamHtEquipmentScrapOrder);
            htList.add(eamHtEquipmentScrapOrder);

            //删除报废单明细
            Example example1 = new Example(EamEquipmentScrapOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentScrapOrderId", id);
            eamEquipmentScrapOrderDetMapper.deleteByExample(example1);
        }

        eamHtEquipmentScrapOrderMapper.insertList(htList);

        return eamEquipmentScrapOrderMapper.deleteByIds(ids);
    }
}
