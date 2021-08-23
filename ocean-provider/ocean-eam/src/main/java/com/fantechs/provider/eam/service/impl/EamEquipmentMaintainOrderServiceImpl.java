package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrder;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMaintainOrderMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaintainOrderMapper;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderDetService;
import com.fantechs.provider.eam.service.EamEquipmentMaintainOrderService;
import com.fantechs.provider.eam.service.EamEquipmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamEquipmentMaintainOrderServiceImpl extends BaseService<EamEquipmentMaintainOrder> implements EamEquipmentMaintainOrderService {

    @Resource
    private EamEquipmentMaintainOrderMapper eamEquipmentMaintainOrderMapper;

    @Resource
    private EamHtEquipmentMaintainOrderMapper eamHtEquipmentMaintainOrderMapper;

    @Resource
    private EamEquipmentService eamEquipmentService;

    @Resource
    private EamEquipmentMaintainOrderDetService eamEquipmentMaintainOrderDetService;

    @Override
    public List<EamEquipmentMaintainOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMaintainOrderMapper.findList(map);
    }

    @Override
    public List<EamEquipmentMaintainOrderDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaintainOrderMapper.findList(map);
    }

    @Override
    public EamEquMaintainOrderDto findListForOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        List<EamEquMaintainOrderDto> list = eamEquipmentService.findListForMaintainOrder(map);
        if (list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此设备条码没有关联设备或者设备类别");
        }
        return list.get(0);
    }

    @Override
    @Transactional
    public int save(EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        // 新增保养单
        eamEquipmentMaintainOrderMapper.insertUseGeneratedKeys(eamEquipmentMaintainOrder);

        // 新增保养单履历
        EamHtEquipmentMaintainOrder eamHtEquipmentMaintainOrder = new EamHtEquipmentMaintainOrder();
        BeanUtil.copyProperties(eamEquipmentMaintainOrder, eamHtEquipmentMaintainOrder);
        eamHtEquipmentMaintainOrderMapper.insert(eamHtEquipmentMaintainOrder);

        if(!eamEquipmentMaintainOrder.getOrderDets().isEmpty()){
            List<EamEquipmentMaintainOrderDet> maintainOrderDets = eamEquipmentMaintainOrder.getOrderDets()
                    .stream()
                    .map(item -> {
                        item.setEquipmentMaintainOrderId(eamEquipmentMaintainOrder.getEquipmentMaintainOrderId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增保养单事项及其履历
            eamEquipmentMaintainOrderDetService.batchSave(maintainOrderDets);
        }
        return 0;
    }

    @Override
    @Transactional
    public int update(EamEquipmentMaintainOrder eamEquipmentMaintainOrder) {
        // 修改保养单
        eamEquipmentMaintainOrderMapper.updateByPrimaryKey(eamEquipmentMaintainOrder);

        // 新增保养单履历
        EamHtEquipmentMaintainOrder eamHtEquipmentMaintainOrder = new EamHtEquipmentMaintainOrder();
        BeanUtil.copyProperties(eamEquipmentMaintainOrder, eamHtEquipmentMaintainOrder);
        eamHtEquipmentMaintainOrderMapper.insert(eamHtEquipmentMaintainOrder);

        if(!eamEquipmentMaintainOrder.getOrderDets().isEmpty()){
            // 批量删除保养单明细
            eamEquipmentMaintainOrderDetService.batchDelete(eamEquipmentMaintainOrder.getOrderDets());
            // 批量新增保养单明细及其履历
            eamEquipmentMaintainOrderDetService.batchSave(eamEquipmentMaintainOrder.getOrderDets());
        }
        return 0;
    }

    @Override
    @Transactional
    public int batchDelete(String ids){
        // 查询保养事项集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderIds", ids);
        List<EamEquipmentMaintainOrderDetDto> list = eamEquipmentMaintainOrderDetService.findList(map);
        if(!list.isEmpty()){
            List<EamEquipmentMaintainOrderDet> projectItems = list.stream().map(item -> {
                EamEquipmentMaintainOrderDet orderDet = new EamEquipmentMaintainOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            // 批量删除保养项目事项
            eamEquipmentMaintainOrderDetService.batchDelete(projectItems);
        }
        // 批量删除保养项目
        return this.batchDelete(ids);
    }

    private SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
