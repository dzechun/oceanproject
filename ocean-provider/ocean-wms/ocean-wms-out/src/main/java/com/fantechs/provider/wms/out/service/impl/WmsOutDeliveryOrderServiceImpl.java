package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsOutDeliveryOrderServiceImpl extends BaseService<WmsOutDeliveryOrder> implements WmsOutDeliveryOrderService {

    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;
    @Resource
    private WmsOutDeliveryOrderDetMapper wmsOutDeliveryOrderDetMapper;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        return wmsOutDeliveryOrderMapper.findList(map);
    }

    @Override
    public int save(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        //出库单明细
        List<WmsOutDeliveryOrderDet> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isEmpty(wmsOutDeliveryOrderDetList)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        wmsOutDeliveryOrder.setDeliveryOrderCode(CodeUtils.getId("XSCK-"));//自动生成销售出库单号
        wmsOutDeliveryOrder.setCreateTime(new Date());
        wmsOutDeliveryOrder.setCreateUserId(user.getUserId());
        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());

        int i = wmsOutDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutDeliveryOrder);

        for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
            wmsOutDeliveryOrderDet.setModifiedTime(new Date());
            wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
            wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());
        }
        wmsOutDeliveryOrderDetMapper.insertList(wmsOutDeliveryOrderDetList);

        return i;
    }

    @Override
    public int update(WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutDeliveryOrder.setModifiedTime(new Date());
        wmsOutDeliveryOrder.setModifiedUserId(user.getUserId());
        wmsOutDeliveryOrder.setOrgId(user.getOrganizationId());

        Example example = new Example(WmsOutDeliveryOrder.class);
        example.createCriteria().andEqualTo("deliveryOrderId", wmsOutDeliveryOrder.getDeliveryOrderId());
        wmsOutDeliveryOrderDetMapper.deleteByExample(example);

        //出库单明细
        List<WmsOutDeliveryOrderDet> wmsOutDeliveryOrderDetList = wmsOutDeliveryOrder.getWmsOutDeliveryOrderDetList();
        if(StringUtils.isNotEmpty(wmsOutDeliveryOrderDetList)){
            for (WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet : wmsOutDeliveryOrderDetList ) {
                wmsOutDeliveryOrderDet.setModifiedTime(new Date());
                wmsOutDeliveryOrderDet.setModifiedUserId(user.getUserId());
                wmsOutDeliveryOrderDet.setOrgId(user.getOrganizationId());
            }
            wmsOutDeliveryOrderDetMapper.insertList(wmsOutDeliveryOrderDetList);
        }

        return wmsOutDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryOrder);
    }

    @Override
    public int batchDelete(String ids) {
        return super.batchDelete(ids);
    }
}
