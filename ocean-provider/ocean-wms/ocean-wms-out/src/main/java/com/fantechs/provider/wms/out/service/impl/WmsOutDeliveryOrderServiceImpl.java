package com.fantechs.provider.wms.out.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutDeliveryOrderServiceImpl  extends BaseService<WmsOutDeliveryOrder> implements WmsOutDeliveryOrderService {

    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;
    @Resource
    private WmsOutHtDeliveryOrderMapper wmsOutHtDeliveryOrderMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        return wmsOutDeliveryOrderMapper.findList(map);
    }

    @Override
    public List<WmsOutDeliveryOrderDto> findHtList(Map<String, Object> map) {
        return wmsOutHtDeliveryOrderMapper.findHtList(map);
    }

    @Override
    public int save(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("CK"));
        wmsOutDeliveryOrder.setOutStatus((byte)0);
        wmsOutDeliveryOrder.setStatus((byte)1);
        wmsOutDeliveryOrder.setIsDelete((byte)1);
        wmsOutDeliveryOrder.setCreateTime(new Date());
        wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrganizationId(user.getOrganizationId());
        int result = wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

        //履历
        WmsOutHtDeliveryOrder wmsOutHtDeliveryOrder = new WmsOutHtDeliveryOrder();
        BeanUtils.copyProperties(wmsOutDeliveryOrder,wmsOutHtDeliveryOrder);
        wmsOutHtDeliveryOrderMapper.insertSelective(wmsOutHtDeliveryOrder);

        for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList()) {
            wmsOutDeliveryOrderDet.setDeliveryOrderId(wmsOutDeliveryOrder.getDeliveryOrderId());
            wmsOutDeliveryOrderDet.setCreateTime(new Date());
            wmsOutDeliveryOrderDet.setCreateUserId(user.getUserId());
            wmsOutDeliveryOrderDetMapper.insertSelective(wmsOutDeliveryOrderDet);
        }

        //修改库存


        return result;
    }
}
