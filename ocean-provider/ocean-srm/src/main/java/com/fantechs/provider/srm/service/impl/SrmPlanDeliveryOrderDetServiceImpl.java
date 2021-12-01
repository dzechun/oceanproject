package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderDetMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderService;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderDetService;
import org.springframework.stereotype.Service;
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
    private SrmInAsnOrderService srmInAsnOrderService;

    @Override
    public List<SrmPlanDeliveryOrderDetDto> findList(Map<String, Object> map) {
        return srmPlanDeliveryOrderDetMapper.findList(map);
    }

    @Override
    public int asn(List<SrmPlanDeliveryOrderDetDto> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isNotEmpty(list)) {

            SrmInAsnOrderDto srmInAsnOrderDto = new SrmInAsnOrderDto();
            List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos = new ArrayList<>();

            Example example = new Example(SrmPlanDeliveryOrderDet.class);
            example.createCriteria().andEqualTo("planDeliveryOrderId",list.get(0).getPlanDeliveryOrderId());
            srmPlanDeliveryOrderDetMapper.deleteByExample(example);
            for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : list) {
                if (srmPlanDeliveryOrderDetDto.getIfCreateAsn() == 1) {
                    continue;
                }
                srmPlanDeliveryOrderDetDto.setIfCreateAsn((byte) 1);
                srmPlanDeliveryOrderDetDto.setModifiedUserId(user.getUserId());
                srmPlanDeliveryOrderDetDto.setModifiedTime(new Date());

                SrmInAsnOrderDetDto srmInAsnOrderDetDto = new SrmInAsnOrderDetDto();
                srmInAsnOrderDetDto.setSourceOrderId(srmPlanDeliveryOrderDetDto.getPurchaseOrderId());
                srmInAsnOrderDetDto.setWarehouseId(srmPlanDeliveryOrderDetDto.getWarehouseId());
                srmInAsnOrderDetDto.setMaterialId(srmPlanDeliveryOrderDetDto.getMaterialId());
                srmInAsnOrderDetDto.setDeliveryQty(srmPlanDeliveryOrderDetDto.getPlanDeliveryQty());
                srmInAsnOrderDetDto.setReceivingDate(srmPlanDeliveryOrderDetDto.getPlanDeliveryDate());
                srmInAsnOrderDetDtos.add(srmInAsnOrderDetDto);
            }
            srmInAsnOrderDto.setSrmInAsnOrderDetDtos(srmInAsnOrderDetDtos);
            srmInAsnOrderService.save(srmInAsnOrderDto);
            srmPlanDeliveryOrderDetMapper.insertList(list);
        }

        return 1;
    }
}