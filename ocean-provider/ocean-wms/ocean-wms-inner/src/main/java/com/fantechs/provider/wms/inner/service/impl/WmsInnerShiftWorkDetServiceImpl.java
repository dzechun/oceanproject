package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.Lei on 2021/05/06.
 */
@Service
public class WmsInnerShiftWorkDetServiceImpl extends BaseService<WmsInnerJobOrderDet> implements WmsInnerShiftWorkDetService {

    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerInventoryService wmsInnerInventoryService;
    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;

    @Override
    public List<WmsInnerJobOrderDetDto> findList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet) {
        if (StringUtils.isEmpty(searchWmsInnerJobOrderDet.getOrgId())) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            searchWmsInnerJobOrderDet.setOrgId(user.getOrganizationId());
        }
        return wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
    }

    @Override
    public int batchUpdate(List<WmsInnerJobOrderDet> list) {
        return wmsInnerJobOrderDetMapper.batchUpdate(list);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int pickDisQty(List<WmsInnerJobOrderDet> wmsInnerJobOrderDets) {
        int num = 0;
        for (WmsInnerJobOrderDet det : wmsInnerJobOrderDets) {
            //领料拣货单
            //拣货数量小于分配数量
            if (det.getActualQty().compareTo(det.getDistributionQty()) == -1) {
                //已分配未拣货数量 = 分配数量-拣货数量
                BigDecimal qty = det.getDistributionQty().subtract(det.getActualQty());
                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(det, wmsInnerJobOrderDet);
                wmsInnerJobOrderDet.setJobOrderDetId(null);
                wmsInnerJobOrderDet.setWorkStartTime(null);
                wmsInnerJobOrderDet.setWorkEndTime(null);
                wmsInnerJobOrderDet.setLineStatus((byte) 3);
                wmsInnerJobOrderDet.setPlanQty(qty);
                wmsInnerJobOrderDet.setDistributionQty(qty);
                wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                if (redisUtil.hasKey("PICKINGID:" + det.getJobOrderDetId())) {
                    Map<Long, BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get("PICKINGID:" + det.getJobOrderDetId().toString());
                    for (Map.Entry<Long, BigDecimal> m : map.entrySet()) {
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
                        wmsInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
                    }
                    //设置3秒后失效
                    redisUtil.expire("PICKINGID:" + det.getJobOrderDetId(), 3);

                    redisUtil.set("PICKINGID:" + wmsInnerJobOrderDet.getJobOrderDetId(), map);
                }
                det.setPlanQty(det.getPlanQty().subtract(qty));
                det.setDistributionQty(det.getDistributionQty().subtract(qty));
                det.setWorkEndTime(new Date());
                wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(det);
            }
        }
        return num;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int batchDeleteByShiftWork(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(s);
            if (StringUtils.isEmpty(wmsInnerJobOrderDet)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
            if (wmsInnerJobOrder.getOrderStatus() != 1) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "单据不是未分配状态，无法删除");
            }
            // 查询明细对应的库存
            Example exampleInventory = new Example(WmsInnerInventory.class);
            exampleInventory.createCriteria().andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
            WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(exampleInventory);
            // 查询明细库存对应的原库存
            WmsInnerInventory sourceInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(innerInventory.getParentInventoryId());
            sourceInnerInventory.setPackingQty(sourceInnerInventory.getPackingQty().add(innerInventory.getPackingQty()));
            // 修改原库存
            wmsInnerInventoryService.update(sourceInnerInventory);
            // 删除明细库存
            wmsInnerInventoryMapper.deleteByPrimaryKey(innerInventory);
        }
        return wmsInnerJobOrderDetMapper.deleteByIds(ids);
    }

}
