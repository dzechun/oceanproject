package com.fantechs.provider.eam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquPointInspectionOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquPointInspectionOrderMapper;
import com.fantechs.provider.eam.mapper.EamHtEquPointInspectionOrderMapper;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderDetService;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquPointInspectionOrderServiceImpl extends BaseService<EamEquPointInspectionOrder> implements EamEquPointInspectionOrderService {

    @Resource
    private EamEquPointInspectionOrderMapper eamEquPointInspectionOrderMapper;

    @Resource
    private EamHtEquPointInspectionOrderMapper eamHtEquPointInspectionOrderMapper;

    @Resource
    private EamEquipmentService eamEquipmentService;

    @Resource
    private EamEquPointInspectionOrderDetService eamEquPointInspectionOrderDetService;

    @Override
    public List<EamEquPointInspectionOrderDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquPointInspectionOrderMapper.findList(map);
    }

    @Override
    public List<EamEquPointInspectionOrderDto> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquPointInspectionOrderMapper.findList(map);
    }

    @Override
    public EamEquInspectionOrderDto findListForOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        List<EamEquInspectionOrderDto> list = eamEquipmentService.findListForInspectionOrder(map);
        if (list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "此设备条码没有关联设备或者设备类别");
        }
        return list.get(0);
    }

    @Override
    @Transactional
    public int save(EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        // 新增点检单
        eamEquPointInspectionOrderMapper.insertUseGeneratedKeys(eamEquPointInspectionOrder);

        // 新增点检单履历
        EamHtEquPointInspectionOrder eamHtEquPointInspectionOrder = new EamHtEquPointInspectionOrder();
        BeanUtil.copyProperties(eamEquPointInspectionOrder, eamHtEquPointInspectionOrder);
        eamHtEquPointInspectionOrderMapper.insert(eamHtEquPointInspectionOrder);

        if(!eamEquPointInspectionOrder.getOrderDets().isEmpty()){
            List<EamEquPointInspectionOrderDet> inspectionOrderDets = eamEquPointInspectionOrder.getOrderDets()
                    .stream()
                    .map(item -> {
                        item.setEquPointInspectionOrderId(eamEquPointInspectionOrder.getEquPointInspectionOrderId());
                        return item;
                    }).collect(Collectors.toList());
            // 批量新增点检单事项及其履历
            eamEquPointInspectionOrderDetService.batchSave(inspectionOrderDets);
        }
        return 0;
    }

    @Override
    @Transactional
    public int update(EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        // 修改点检单
        eamEquPointInspectionOrderMapper.updateByPrimaryKey(eamEquPointInspectionOrder);

        // 新增点检单履历
        EamHtEquPointInspectionOrder eamHtEquPointInspectionOrder = new EamHtEquPointInspectionOrder();
        BeanUtil.copyProperties(eamEquPointInspectionOrder, eamHtEquPointInspectionOrder);
        eamHtEquPointInspectionOrderMapper.insert(eamHtEquPointInspectionOrder);

        if(!eamEquPointInspectionOrder.getOrderDets().isEmpty()){
            // 批量删除点检单明细
            eamEquPointInspectionOrderDetService.batchDelete(eamEquPointInspectionOrder.getOrderDets());
            // 批量新增点检单明细及其履历
            eamEquPointInspectionOrderDetService.batchSave(eamEquPointInspectionOrder.getOrderDets());
        }
        return 0;
    }

    @Override
    @Transactional
    public int batchDelete(String ids){
        // 查询点检事项集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderIds", ids);
        List<EamEquPointInspectionOrderDetDto> list = eamEquPointInspectionOrderDetService.findList(map);
        if(!list.isEmpty()){
            List<EamEquPointInspectionOrderDet> projectItems = list.stream().map(item -> {
                EamEquPointInspectionOrderDet orderDet = new EamEquPointInspectionOrderDet();
                BeanUtil.copyProperties(item, orderDet);
                return orderDet;
            }).collect(Collectors.toList());
            // 批量删除点检项目事项
            eamEquPointInspectionOrderDetService.batchDelete(projectItems);
        }
        // 批量删除点检项目
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
