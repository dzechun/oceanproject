package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquRepairOrderReplacementDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentRepairOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentRepairOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentRepairOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRepairOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentRepairOrderService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquipmentRepairOrderServiceImpl extends BaseService<EamEquipmentRepairOrder> implements EamEquipmentRepairOrderService {

    @Resource
    private EamEquipmentRepairOrderMapper eamEquipmentRepairOrderMapper;
    @Resource
    private EamHtEquipmentRepairOrderMapper eamHtEquipmentRepairOrderMapper;
    @Resource
    private EamEquRepairOrderReplacementMapper eamEquRepairOrderReplacementMapper;
    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;


    @Override
    public List<EamEquipmentRepairOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamEquipmentRepairOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamEquipmentRepairOrderDto pdaCreateOrder(String equipmentBarcode) {
        Example example = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBarcode",equipmentBarcode);
        List<EamEquipmentBarcode> eamEquipmentBarcodes = eamEquipmentBarcodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(eamEquipmentBarcodes)){
            throw new BizErrorException("查不到该设备条码");
        }

        SearchEamEquipmentRepairOrder searchEamEquipmentRepairOrder = new SearchEamEquipmentRepairOrder();
        searchEamEquipmentRepairOrder.setEquipmentBarcodeId(eamEquipmentBarcodes.get(0).getEquipmentBarcodeId());
        searchEamEquipmentRepairOrder.setOrderStatus((byte)1);
        List<EamEquipmentRepairOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentRepairOrder));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该设备待维修状态的单据");
        }

        //保存
        EamEquipmentRepairOrder eamEquipmentRepairOrder = new EamEquipmentRepairOrder();
        eamEquipmentRepairOrder.setEquipmentBarcode(equipmentBarcode);
        eamEquipmentRepairOrder.setEquipmentId(eamEquipmentBarcodes.get(0).getEquipmentId());
        eamEquipmentRepairOrder.setEquipmentBarcodeId(eamEquipmentBarcodes.get(0).getEquipmentBarcodeId());
        eamEquipmentRepairOrder.setRequestForRepairTime(new Date());
        EamEquipmentRepairOrderDto eamEquipmentRepairOrderDto = new EamEquipmentRepairOrderDto();
        BeanUtils.copyProperties(eamEquipmentRepairOrder,eamEquipmentRepairOrderDto);
        this.save(eamEquipmentRepairOrderDto);

        SearchEamEquipmentRepairOrder searchEamEquipmentRepairOrder1 = new SearchEamEquipmentRepairOrder();
        searchEamEquipmentRepairOrder1.setEquipmentRepairOrderId(eamEquipmentRepairOrderDto.getEquipmentRepairOrderId());
        List<EamEquipmentRepairOrderDto> list = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentRepairOrder1));

        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentRepairOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setEquipmentRepairOrderCode(CodeUtils.getId("SBWX-"));
        record.setRequestForRepairTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus((byte)1);
        eamEquipmentRepairOrderMapper.insertUseGeneratedKeys(record);

        //设备维修单替换件
        List<EamEquRepairOrderReplacementDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamEquRepairOrderReplacementDto eamEquRepairOrderReplacementDto : list){
                eamEquRepairOrderReplacementDto.setEquipmentRepairOrderId(record.getEquipmentRepairOrderId());
                eamEquRepairOrderReplacementDto.setCreateUserId(user.getUserId());
                eamEquRepairOrderReplacementDto.setCreateTime(new Date());
                eamEquRepairOrderReplacementDto.setModifiedUserId(user.getUserId());
                eamEquRepairOrderReplacementDto.setModifiedTime(new Date());
                eamEquRepairOrderReplacementDto.setStatus(StringUtils.isEmpty(eamEquRepairOrderReplacementDto.getStatus())?1: eamEquRepairOrderReplacementDto.getStatus());
                eamEquRepairOrderReplacementDto.setOrgId(user.getOrganizationId());
            }
            eamEquRepairOrderReplacementMapper.insertList(list);
        }

        //修改设备状态为维修中
        EamEquipmentBarcode eamEquipmentBarcode = eamEquipmentBarcodeMapper.selectByPrimaryKey(record.getEquipmentBarcodeId());
        eamEquipmentBarcode.setEquipmentStatus((byte)8);
        eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);

        EamHtEquipmentRepairOrder eamHtEquipmentRepairOrder = new EamHtEquipmentRepairOrder();
        BeanUtils.copyProperties(record,eamHtEquipmentRepairOrder);
        int i = eamHtEquipmentRepairOrderMapper.insert(eamHtEquipmentRepairOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentRepairOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setRepairTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamEquipmentRepairOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原设备维修单替换件
        Example example1 = new Example(EamEquRepairOrderReplacement.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentRepairOrderId", entity.getEquipmentRepairOrderId());
        eamEquRepairOrderReplacementMapper.deleteByExample(example1);

        //设备维修单替换件
        List<EamEquRepairOrderReplacementDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamEquRepairOrderReplacementDto eamEquRepairOrderReplacementDto : list){
                eamEquRepairOrderReplacementDto.setEquipmentRepairOrderId(entity.getEquipmentRepairOrderId());
                eamEquRepairOrderReplacementDto.setCreateUserId(user.getUserId());
                eamEquRepairOrderReplacementDto.setCreateTime(new Date());
                eamEquRepairOrderReplacementDto.setModifiedUserId(user.getUserId());
                eamEquRepairOrderReplacementDto.setModifiedTime(new Date());
                eamEquRepairOrderReplacementDto.setStatus(StringUtils.isEmpty(eamEquRepairOrderReplacementDto.getStatus())?1: eamEquRepairOrderReplacementDto.getStatus());
                eamEquRepairOrderReplacementDto.setOrgId(user.getOrganizationId());
            }
            eamEquRepairOrderReplacementMapper.insertList(list);
        }

        EamHtEquipmentRepairOrder eamHtEquipmentRepairOrder = new EamHtEquipmentRepairOrder();
        BeanUtils.copyProperties(entity,eamHtEquipmentRepairOrder);
        int i = eamHtEquipmentRepairOrderMapper.insert(eamHtEquipmentRepairOrder);

        updateEquipmentStatus(entity);

        return i;
    }

    /**
     * 修改该设备使用状态
     * @param entity
     */
    public void updateEquipmentStatus(EamEquipmentRepairOrderDto entity) {
        if(StringUtils.isNotEmpty(entity.getRepairUserId())) {
            entity.setOrderStatus((byte)3);
            eamEquipmentRepairOrderMapper.updateByPrimaryKeySelective(entity);

            EamEquipmentBarcode eamEquipmentBarcode = new EamEquipmentBarcode();
            eamEquipmentBarcode.setEquipmentBarcodeId(entity.getEquipmentBarcodeId());
            eamEquipmentBarcode.setEquipmentStatus((byte) 5);
            eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtEquipmentRepairOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamEquipmentRepairOrder eamEquipmentRepairOrder = eamEquipmentRepairOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamEquipmentRepairOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipmentRepairOrder eamHtEquipmentRepairOrder = new EamHtEquipmentRepairOrder();
            BeanUtils.copyProperties(eamEquipmentRepairOrder,eamHtEquipmentRepairOrder);
            htList.add(eamHtEquipmentRepairOrder);

            //删除设备维修单替换件
            Example example1 = new Example(EamEquRepairOrderReplacement.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentRepairOrderId", id);
            eamEquRepairOrderReplacementMapper.deleteByExample(example1);
        }

        eamHtEquipmentRepairOrderMapper.insertList(htList);

        return eamEquipmentRepairOrderMapper.deleteByIds(ids);
    }

}
