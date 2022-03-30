package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.InStorageMaterialDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.Lei on 2021/06/02.
 */
@Service
public class WmsInnerInventoryDetServiceImpl extends BaseService<WmsInnerInventoryDet> implements WmsInnerInventoryDetService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

    @Override
    public List<WmsInnerInventoryDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        return wmsInnerInventoryDetMapper.findList(map);
    }

    /**
     * 加库存明细
     *
     * @param wmsInnerInventoryDets
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(List<WmsInnerInventoryDet> wmsInnerInventoryDets) {
        SysUser sysUser = currentUser();
        for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
            wmsInnerInventoryDet.setCreateTime(new Date());
            wmsInnerInventoryDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setModifiedTime(new Date());
            wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int update(WmsInnerInventoryDet entity) {
        return super.update(entity);
    }

    /**
     * 减库存明细
     *
     * @param wmsInnerInventoryDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int subtract(WmsInnerInventoryDet wmsInnerInventoryDet) {
        SysUser sysUser = currentUser();
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isEmpty(wmsInnerInventoryDet.getMaterialQty()) || wmsInnerInventoryDet.getMaterialQty().compareTo(BigDecimal.ZERO) < 1) {
            throw new BizErrorException("出库数量错误");
        }
        if (StringUtils.isNotEmpty(wmsInnerInventoryDet.getBarcode())) {
            criteria.andEqualTo("barcode", wmsInnerInventoryDet.getBarcode());
        }
        if (StringUtils.isNotEmpty(wmsInnerInventoryDet.getStorageId())) {
            criteria.andEqualTo("storageId", wmsInnerInventoryDet.getStorageId());
        }
        if (StringUtils.isNotEmpty(wmsInnerInventoryDet.getMaterialId())) {
            criteria.andEqualTo("materialId", wmsInnerInventoryDet.getMaterialId());
        }
        if (StringUtils.isNotEmpty(wmsInnerInventoryDet.getAsnCode())) {
            criteria.andEqualTo("asnCode", wmsInnerInventoryDet.getAsnCode());
        }
        if (StringUtils.isNotEmpty(wmsInnerInventoryDet.getDeliveryOrderCode())) {
            criteria.andEqualTo("deliveryOrderCode", wmsInnerInventoryDet.getDeliveryOrderCode());
        }
        criteria.andEqualTo("orgId", sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wms = wmsInnerInventoryDetMapper.selectByExample(example);
        BigDecimal qty = wmsInnerInventoryDet.getMaterialQty();
        int num = 0;
        for (WmsInnerInventoryDet wm : wms) {
            if (qty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            if (qty.compareTo(wmsInnerInventoryDet.getMaterialQty()) == 1) {
                if (qty.compareTo(wmsInnerInventoryDet.getMaterialQty()) == 1) {
                    //删除记录
                    wmsInnerInventoryDetMapper.deleteByPrimaryKey(wmsInnerInventoryDet.getInventoryDetId());
                } else {
                    wmsInnerInventoryDet.setMaterialQty(wmsInnerInventoryDet.getMaterialQty().subtract(qty));
                }
                qty.subtract(wmsInnerInventoryDet.getMaterialQty());
            }
            wm.setBarcodeStatus((byte) 6);
            num += wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wm);
        }
        return num;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(sysUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }

    @Override
    public WmsInnerInventoryDet findByOne(String barCode) {
        SysUser sysUser = currentUser();
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode", barCode).andEqualTo("orgId", sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectByExample(example);
        if (wmsInnerInventoryDet.size() > 0) {
            return wmsInnerInventoryDet.get(0);
        }
        return null;
    }

    @Override
    public List<InStorageMaterialDto> findInventoryDetByStorage(Map<String, Object> map) {
        return wmsInnerInventoryDetMapper.findInventoryDetByStorage(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int lock(String ids) {
        int i = 0;
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            Example example = new Example(WmsInnerInventoryDet.class);
            example.createCriteria()
                    .andEqualTo("inventoryDetId", s)
                    .andEqualTo("ifStockLock", 0)
                    .andEqualTo("barcodeStatus", 3);
            WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(wmsInnerInventoryDet)) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码已盘点锁定或已出库");
            }
            if (wmsInnerInventoryDet.getLockStatus() == 0) {
                wmsInnerInventoryDet.setLockStatus((byte) 1);
                wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventoryDet.setModifiedTime(new Date());
                i = wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
            } else {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前库存明细已被锁定");
            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int unlock(String ids) {
        int i = 0;
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            Example example = new Example(WmsInnerInventoryDet.class);
            example.createCriteria().andEqualTo("inventoryDetId", s);
            WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
            if (wmsInnerInventoryDet.getLockStatus() == 1) {
                wmsInnerInventoryDet.setLockStatus((byte) 0);
                wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventoryDet.setModifiedTime(new Date());
                i = wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
            } else {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "当前库存明细未被锁定");
            }
        }
        return i;
    }
}
