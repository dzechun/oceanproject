package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanDeliveryOrderDetServiceImpl extends BaseService<WmsOutPlanDeliveryOrderDet> implements WmsOutPlanDeliveryOrderDetService {

    @Resource
    private WmsOutPlanDeliveryOrderDetMapper wmsOutPlanDeliveryOrderDetMapper;
    @Resource
    private WmsOutPlanDeliveryOrderMapper wmsOutPlanDeliveryOrderMapper;
    @Resource
    private WmsOutHtPlanDeliveryOrderDetMapper wmsOutHtPlanDeliveryOrderDetMapper;

    @Override
    public List<WmsOutPlanDeliveryOrderDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutPlanDeliveryOrderDetMapper.findList(map);
    }

    @Override
    public List<WmsOutHtPlanDeliveryOrderDet> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutHtPlanDeliveryOrderDetMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateActualQty(WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto) {
        if(wmsOutPlanDeliveryOrderDetDto.getActualQty().compareTo(BigDecimal.ZERO)==1
                &&wmsOutPlanDeliveryOrderDetDto.getActualQty().compareTo(wmsOutPlanDeliveryOrderDetDto.getTotalIssueQty())==-1){
            wmsOutPlanDeliveryOrderDetDto.setLineStatus((byte)2);
        }
        if(wmsOutPlanDeliveryOrderDetDto.getActualQty().compareTo(wmsOutPlanDeliveryOrderDetDto.getTotalIssueQty())==0){
            wmsOutPlanDeliveryOrderDetDto.setLineStatus((byte)3);
        }
        int i = wmsOutPlanDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutPlanDeliveryOrderDetDto);

        WmsOutPlanDeliveryOrder wmsOutPlanDeliveryOrder = wmsOutPlanDeliveryOrderMapper.selectByPrimaryKey(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderId());
        Example example = new Example(WmsOutPlanDeliveryOrderDet.class);
        example.createCriteria().andEqualTo("planDeliveryOrderId",wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderId());
        List<WmsOutPlanDeliveryOrderDet> wmsOutPlanDeliveryOrderDets = wmsOutPlanDeliveryOrderDetMapper.selectByExample(example);

        wmsOutPlanDeliveryOrder.setOrderStatus((byte)3);
        for (WmsOutPlanDeliveryOrderDet wmsOutPlanDeliveryOrderDet : wmsOutPlanDeliveryOrderDets){
            if(wmsOutPlanDeliveryOrderDet.getLineStatus()!=(byte)3){
                wmsOutPlanDeliveryOrder.setOrderStatus((byte)2);
                break;
            }
        }
        wmsOutPlanDeliveryOrderMapper.updateByPrimaryKeySelective(wmsOutPlanDeliveryOrder);

        return i;
    }
}
