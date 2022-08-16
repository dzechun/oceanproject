package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerJobOrderDetServiceImpl extends BaseService<WmsInnerJobOrderDet> implements WmsInnerJobOrderDetService {

    @Resource
    private WmsInnerJobOrderDetMapper wmsInPutawayOrderDetMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInnerJobOrderDetDto> findList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        searchWmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
        if (searchWmsInnerJobOrderDet.getJobOrderType() == (byte) 3){
            searchWmsInnerJobOrderDet.setUserId(sysUser.getUserId());
        }
        return wmsInPutawayOrderDetMapper.findList(searchWmsInnerJobOrderDet);
    }

    @Override
    public int batchUpdate(List<WmsInnerJobOrderDet> list) {
        return wmsInPutawayOrderDetMapper.batchUpdate(list);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pickDisQty(List<WmsInnerJobOrderDet> wmsInnerJobOrderDets) {
        int num = 0;
        for (WmsInnerJobOrderDet det : wmsInnerJobOrderDets) {
            //领料拣货单
            //拣货数量小于分配数量
            if(det.getActualQty().compareTo(det.getDistributionQty())==-1){
                //已分配未拣货数量 = 分配数量-拣货数量
                BigDecimal qty = det.getDistributionQty().subtract(det.getActualQty());
                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(det,wmsInnerJobOrderDet);
                wmsInnerJobOrderDet.setJobOrderDetId(null);
                wmsInnerJobOrderDet.setWorkStartTime(null);
                wmsInnerJobOrderDet.setWorkEndTime(null);
                wmsInnerJobOrderDet.setOrderStatus((byte)3);
                wmsInnerJobOrderDet.setPlanQty(qty);
                wmsInnerJobOrderDet.setDistributionQty(qty);
                wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                wmsInPutawayOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                if(redisUtil.hasKey("PICKINGID:"+det.getJobOrderDetId())){
                    Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get("PICKINGID:"+det.getJobOrderDetId().toString());
                    for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
                        wmsInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }
                    //设置3秒后失效
                    redisUtil.expire("PICKINGID:"+det.getJobOrderDetId(),3);

                    redisUtil.set("PICKINGID:"+wmsInnerJobOrderDet.getJobOrderDetId(),map);
                }
                det.setPlanQty(det.getPlanQty().subtract(qty));
                det.setDistributionQty(det.getDistributionQty().subtract(qty));
                det.setWorkEndTime(new Date());
                wmsInPutawayOrderDetMapper.updateByPrimaryKeySelective(det);
            }
        }
        return num;
    }
}
