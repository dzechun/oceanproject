package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryReqOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryReqOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtDeliveryReqOrderDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryReqOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@Service
public class WmsOutDeliveryReqOrderDetServiceImpl extends BaseService<WmsOutDeliveryReqOrderDet> implements WmsOutDeliveryReqOrderDetService {

    @Resource
    private WmsOutDeliveryReqOrderDetMapper wmsOutDeliveryReqOrderDetMapper;
    @Resource
    private WmsOutHtDeliveryReqOrderDetMapper wmsOutHtDeliveryReqOrderDetMapper;
    @Resource
    private WmsOutDeliveryReqOrderMapper wmsOutDeliveryReqOrderMapper;

    @Override
    public List<WmsOutDeliveryReqOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutDeliveryReqOrderDetMapper.findList(map);
    }

    @Override
    public List<WmsOutHtDeliveryReqOrderDet> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutHtDeliveryReqOrderDetMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateActualQty(WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto) {
        if(wmsOutDeliveryReqOrderDetDto.getActualQty().compareTo(BigDecimal.ZERO)==1
                &&wmsOutDeliveryReqOrderDetDto.getActualQty().compareTo(wmsOutDeliveryReqOrderDetDto.getTotalIssueQty())==-1){
            wmsOutDeliveryReqOrderDetDto.setLineStatus((byte)2);
        }
        if(wmsOutDeliveryReqOrderDetDto.getActualQty().compareTo(wmsOutDeliveryReqOrderDetDto.getTotalIssueQty())==0){
            wmsOutDeliveryReqOrderDetDto.setLineStatus((byte)3);
        }
        int i = wmsOutDeliveryReqOrderDetMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrderDetDto);

        WmsOutDeliveryReqOrder wmsOutDeliveryReqOrder = wmsOutDeliveryReqOrderMapper.selectByPrimaryKey(wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderId());
        Example example = new Example(WmsOutDeliveryReqOrderDet.class);
        example.createCriteria().andEqualTo("deliveryReqOrderId",wmsOutDeliveryReqOrderDetDto.getDeliveryReqOrderId());
        List<WmsOutDeliveryReqOrderDet> wmsOutDeliveryReqOrderDets = wmsOutDeliveryReqOrderDetMapper.selectByExample(example);

        wmsOutDeliveryReqOrder.setOrderStatus((byte)3);
        for (WmsOutDeliveryReqOrderDet wmsOutDeliveryReqOrderDet : wmsOutDeliveryReqOrderDets){
            if(wmsOutDeliveryReqOrderDet.getLineStatus()!=(byte)3){
                wmsOutDeliveryReqOrder.setOrderStatus((byte)2);
                break;
            }
        }
        wmsOutDeliveryReqOrderMapper.updateByPrimaryKeySelective(wmsOutDeliveryReqOrder);

        return i;
    }

}
