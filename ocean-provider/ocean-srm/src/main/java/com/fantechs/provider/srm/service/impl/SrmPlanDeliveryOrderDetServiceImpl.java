package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderService;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderDetService;
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
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmPlanDeliveryOrderDetServiceImpl extends BaseService<SrmPlanDeliveryOrderDet> implements SrmPlanDeliveryOrderDetService {

    @Resource
    private SrmPlanDeliveryOrderDetMapper srmPlanDeliveryOrderDetMapper;
    @Resource
    private SrmPlanDeliveryOrderMapper srmPlanDeliveryOrderMapper;
    @Resource
    private SrmInAsnOrderService srmInAsnOrderService;

    @Override
    public List<SrmPlanDeliveryOrderDetDto> findList(Map<String, Object> map) {
        if (StringUtils.isEmpty(map.get("planDeliveryOrderId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("supplierId",user.getSupplierId());
        }
        return srmPlanDeliveryOrderDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int asn(List<SrmPlanDeliveryOrderDetDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isNotEmpty(list)) {


            SrmInAsnOrderDto srmInAsnOrderDto = new SrmInAsnOrderDto();

            Example example1 = new Example(SrmPlanDeliveryOrder.class);
            example1.createCriteria().andEqualTo("planDeliveryOrderId",list.get(0).getPlanDeliveryOrderId());
            List<SrmPlanDeliveryOrder> srmPlanDeliveryOrders = srmPlanDeliveryOrderMapper.selectByExample(example1);
            if (StringUtils.isNotEmpty(srmPlanDeliveryOrders)) {
                srmInAsnOrderDto.setSupplierId(srmPlanDeliveryOrders.get(0).getSupplierId());
            }
            srmInAsnOrderDto.setOrderStatus((byte) 1);
            srmInAsnOrderDto.setSourceOrderId(srmPlanDeliveryOrders.get(0).getPlanDeliveryOrderId());
            srmInAsnOrderDto.setSourceSysOrderTypeCode(srmPlanDeliveryOrders.get(0).getSysOrderTypeCode());
            srmInAsnOrderDto.setCorSourceSysOrderTypeCode(srmPlanDeliveryOrders.get(0).getCorSourceSysOrderTypeCode());


            List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos = new ArrayList<>();
            List<Long> idList = new ArrayList<>();
            for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : list) {
                if (srmPlanDeliveryOrderDetDto.getIfCreateAsn() == 1) {
                    continue;
                }
                idList.add(srmPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId());
                srmPlanDeliveryOrderDetDto.setIfCreateAsn((byte) 1);
                srmPlanDeliveryOrderDetDto.setModifiedUserId(user.getUserId());
                srmPlanDeliveryOrderDetDto.setModifiedTime(new Date());

                SrmInAsnOrderDetDto srmInAsnOrderDetDto = new SrmInAsnOrderDetDto();
                srmInAsnOrderDetDto.setSourceOrderId(srmPlanDeliveryOrderDetDto.getPurchaseOrderDetId());
                srmInAsnOrderDetDto.setSourceId(srmPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId());
                srmInAsnOrderDetDto.setCoreSourceId(srmPlanDeliveryOrderDetDto.getPurchaseOrderDetId());
                srmInAsnOrderDetDto.setCoreSourceOrderCode(srmPlanDeliveryOrderDetDto.getPurchaseOrderCode());
                srmInAsnOrderDetDto.setWarehouseId(srmPlanDeliveryOrderDetDto.getWarehouseId());
                srmInAsnOrderDetDto.setMaterialId(srmPlanDeliveryOrderDetDto.getMaterialId());
                srmInAsnOrderDetDto.setDeliveryQty(srmPlanDeliveryOrderDetDto.getPlanDeliveryQty());
                srmInAsnOrderDetDto.setReceivingDate(srmPlanDeliveryOrderDetDto.getPlanDeliveryDate());
                srmInAsnOrderDetDto.setOrderQty(srmPlanDeliveryOrderDetDto.getOrderQty());
                srmInAsnOrderDetDto.setTotalDeliveryQty(srmPlanDeliveryOrderDetDto.getTotalPlanDeliveryQty());
                srmInAsnOrderDetDtos.add(srmInAsnOrderDetDto);
            }
            if (StringUtils.isNotEmpty(idList)) {
                Example example = new Example(SrmPlanDeliveryOrderDet.class);
                example.createCriteria().andEqualTo("planDeliveryOrderId",list.get(0).getPlanDeliveryOrderId())
                        .andIn("planDeliveryOrderDetId",idList);
                srmPlanDeliveryOrderDetMapper.deleteByExample(example);
            }

            srmInAsnOrderDto.setSrmInAsnOrderDetDtos(srmInAsnOrderDetDtos);
            srmInAsnOrderService.save(srmInAsnOrderDto);
            srmPlanDeliveryOrderDetMapper.insertList(list);
        }

        return 1;
    }
}
