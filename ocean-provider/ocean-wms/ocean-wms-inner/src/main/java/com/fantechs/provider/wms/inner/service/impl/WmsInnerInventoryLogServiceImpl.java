package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryLogMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/07/29.
 */
@Service
public class WmsInnerInventoryLogServiceImpl extends BaseService<WmsInnerInventoryLog> implements WmsInnerInventoryLogService {

    @Resource
    private WmsInnerInventoryLogMapper wmsInnerInventoryLogMapper;

    @Override
    public List<WmsInnerInventoryLogDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInnerInventoryLogMapper.findList(map);
    }

    @Override
    public BigDecimal findInv(Map<String, Object> map) {
        return wmsInnerInventoryLogMapper.findInv(map);
    }

    @Override
    public String findInvName(Long inventoryStatusId) {
        return wmsInnerInventoryLogMapper.findInvName(inventoryStatusId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerInventoryLog record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());

        record.setInventoryStatusName(wmsInnerInventoryLogMapper.findInvName(record.getInventoryStatusId()));
        //期初数量
//        Map<String,Object> map = paramUtil(record);
//        record.setInitialQty(wmsInnerInventoryLogMapper.findInv(map));
        if(StringUtils.isEmpty(record.getInitialQty())){
            record.setInitialQty(BigDecimal.ZERO);
        }
        if(StringUtils.isEmpty(record.getChangeQty())){
            record.setChangeQty(BigDecimal.ZERO);
        }
        if(record.getAddOrSubtract()==1){
            if(StringUtils.isEmpty(record.getInitialQty())){
                record.setInitialQty(BigDecimal.ZERO);
            }
            record.setFinalQty(record.getInitialQty().add(record.getChangeQty()));
        }else{
            if(StringUtils.isNotEmpty(record.getInitialQty())){
                record.setFinalQty(record.getInitialQty().subtract(record.getChangeQty()));
            }
        }

        return super.save(record);
    }

    private static Map<String,Object> paramUtil(WmsInnerInventoryLog wmsInnerInventoryLog){
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",wmsInnerInventoryLog.getWarehouseId());
        map.put("storageId",wmsInnerInventoryLog.getStorageId());
        map.put("materialId",wmsInnerInventoryLog.getMaterialId());
        map.put("batchCode",wmsInnerInventoryLog.getBatchCode());
        map.put("inventoryStatusId",wmsInnerInventoryLog.getInventoryStatusId());
        map.put("orgId",wmsInnerInventoryLog.getOrgId());
        return map;
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
