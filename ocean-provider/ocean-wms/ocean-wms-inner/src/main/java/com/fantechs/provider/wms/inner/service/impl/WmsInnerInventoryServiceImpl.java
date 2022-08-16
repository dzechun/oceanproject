package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.NotOrderInStorage;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsHtInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsInnerInventoryServiceImpl extends BaseService<WmsInnerInventory> implements WmsInnerInventoryService {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Resource
    private WmsHtInnerInventoryMapper wmsHtInnerInventoryMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

    @Override
    public List<WmsInnerInventoryDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInnerInventoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int lock(Long id, BigDecimal quantity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("inventoryId",id);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (wmsInnerInventory.getLockStatus() == 0){
            if (StringUtils.isNotEmpty(wmsInnerInventory) && wmsInnerInventory.getPackingQty().compareTo(quantity) >= 0){
                WmsInnerInventory innerInventory = new WmsInnerInventory();
                BeanUtils.copyProperties(wmsInnerInventory, innerInventory);
                innerInventory.setPackingQty(quantity);
                //innerInventory.setInventoryTotalQty(quantity.multiply(wmsInnerInventory.getPackageSpecificationQuantity()));
                innerInventory.setParentInventoryId(id);
                innerInventory.setInventoryId(null);
                innerInventory.setLockStatus((byte) 1);
                this.save(innerInventory);

                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(quantity));
                //wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty().subtract(innerInventory.getInventoryTotalQty()));
                this.update(wmsInnerInventory);
            }else {
                throw new BizErrorException("库存数量不足");
            }
        }else {
            throw new BizErrorException("当前库存已被锁定");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int unlock(Long id, BigDecimal quantity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("inventoryId",id);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerInventory) && wmsInnerInventory.getLockStatus() == 1) {
            if (quantity.compareTo(wmsInnerInventory.getPackingQty()) == 1){
                throw new BizErrorException("数量超出锁定的数量");
            } else if (quantity.compareTo(wmsInnerInventory.getPackingQty()) == 0){
                wmsInnerInventory.setLockStatus((byte) 0);
                wmsInnerInventory.setPackingQty(new BigDecimal(0));
                //wmsInnerInventory.setInventoryTotalQty(new BigDecimal(0));
            } else {
                WmsInnerInventory innerInventory = new WmsInnerInventory();
                BeanUtils.copyProperties(wmsInnerInventory,innerInventory);
                innerInventory.setPackingQty(quantity);
                //innerInventory.setInventoryTotalQty(quantity.multiply(wmsInnerInventory.getPackageSpecificationQuantity()));
                innerInventory.setParentInventoryId(id);
                innerInventory.setInventoryId(null);
                innerInventory.setLockStatus((byte) 0);
                this.save(innerInventory);
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(quantity));
                //wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty().subtract(innerInventory.getInventoryTotalQty()));
            }
            this.update(wmsInnerInventory);
            example.clear();
            example.createCriteria().andEqualTo("inventoryId",wmsInnerInventory.getParentInventoryId());
            wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(quantity));
            //wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty().add(quantity.multiply(wmsInnerInventory.getPackageSpecificationQuantity())));
            this.update(wmsInnerInventory);
        } else {
            throw new BizErrorException("当前库存未进行锁定");
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerInventory selectOneByExample(Map<String,Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",map.get("materialId")).andEqualTo("batchCode",map.get("batchCode"));
        if(!StringUtils.isEmpty(map.get("relevanceOrderCode"))){
            criteria.andEqualTo("relevanceOrderCode",map.get("relevanceOrderCode"));
        }
        if(!StringUtils.isEmpty(map.get("warehouseId"))){
            criteria.andEqualTo("warehouseId",map.get("warehouseId"));
        }
        if(!StringUtils.isEmpty(map.get("storageId"))){
            criteria.andEqualTo("storageId",map.get("storageId"));
        }
        if(!StringUtils.isEmpty(map.get("jobOrderDetId")) && !StringUtils.isEmpty("jobStatus")){
            criteria.andEqualTo("jobOrderDetId",map.get("jobOrderDetId")).andEqualTo("jobStatus",map.get("jobStatus"));
        }
        if(!StringUtils.isEmpty(map.get("inventoryStatusId"))){
            criteria.andEqualTo("inventoryStatusId",map.get("inventoryStatusId"));
        }
        if(map.containsKey("jobOrderDetId") && StringUtils.isNotEmpty(map.get("jobOrderDetId"))){
            criteria.andEqualTo("jobOrderDetId",map.get("jobOrderDetId"));
        }
        criteria.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        return wmsInnerInventorys;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int updateByPrimaryKeySelective(WmsInnerInventory wmsInnerInventory) {
        return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int updateByExampleSelective(WmsInnerInventory wmsInnerInventory, Map<String,Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",map.get("relevanceOrderCode")).andEqualTo("materialId",map.get("materialId"));
        if(!StringUtils.isEmpty(map.get("batchCode"))){
            criteria.andEqualTo("batchCode",map.get("batchCode"));
        }
        if(!StringUtils.isEmpty(map.get("storageId")) && !StringUtils.isEmpty(map.get("warehouseId"))){
            criteria.andEqualTo("storageId",map.get("storageId")).andEqualTo("warehouseId",map.get("warehouseId"));
        }
        if(!StringUtils.isEmpty(map.get("jobOrderDetId")) && !StringUtils.isEmpty("jobStatus")){
            criteria.andEqualTo("jobOrderDetId",map.get("jobOrderDetId")).andEqualTo("jobStatus",map.get("jobStatus"));
        }
        criteria.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
        wmsInnerInventory.setPackingQty(new BigDecimal(Double.parseDouble(map.get("actualQty").toString())
        ));
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        int num = wmsInnerInventoryMapper.updateByExampleSelective(wmsInnerInventory, example);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int insertSelective(WmsInnerInventory wmsInnerInventory) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
        wmsInnerInventory.setCreateTime(new Date());
        wmsInnerInventory.setCreateUserId(sysUser.getUserId());
        wmsInnerInventory.setModifiedTime(new Date());
        wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
        return wmsInnerInventoryMapper.insertSelective(wmsInnerInventory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int insertList(List<WmsInnerInventory> wmsInnerInventories) {
        return wmsInnerInventoryMapper.insertList(wmsInnerInventories);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchUpdate(List<WmsInnerInventory> list) {
        return wmsInnerInventoryMapper.batchUpdate(list);
    }

    @Override
    public List<WmsInnerInventoryDto> findInvStorage(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        map.put("isStorage","1");
        return wmsInnerInventoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int notOrderInStorage(NotOrderInStorage notOrderInStorage) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        //获取A16库位
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageCode("Z-A16");
        searchBaseStorage.setCodeQueryMark((byte)1);
        ResponseEntity<List<BaseStorage>> responseEntity = baseFeignApi.findList(searchBaseStorage);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        List<BaseStorage> storages = responseEntity.getData();
        if(storages.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取A16库位失败");
        }
        //获取默认库存状态
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setIfDefaultStatus((byte)1);
        ResponseEntity<List<BaseInventoryStatus>> res = baseFeignApi.findList(searchBaseInventoryStatus);
        if(res.getCode()!=0){
            throw new BizErrorException(res.getCode(),res.getMessage());
        }
        List<BaseInventoryStatus> baseInventoryStatuses = res.getData();
        if(baseInventoryStatuses.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取默认库位失败");
        }
        WmsInnerInventory wmsInnerInventory = new WmsInnerInventory();
        wmsInnerInventory.setPackingUnitName(notOrderInStorage.getPackingUnitName());
        wmsInnerInventory.setPackingQty(notOrderInStorage.getPackingQty());
        wmsInnerInventory.setWarehouseId(storages.get(0).getWarehouseId());
        wmsInnerInventory.setStorageId(storages.get(0).getStorageId());
        wmsInnerInventory.setJobStatus((byte)1);
        wmsInnerInventory.setLockStatus((byte)0);
        wmsInnerInventory.setQcLock((byte)0);
        wmsInnerInventory.setQcLock((byte)0);
        wmsInnerInventory.setMaterialId(notOrderInStorage.getMaterialId());
        wmsInnerInventory.setInventoryStatusId(baseInventoryStatuses.get(0).getInventoryStatusId());
        wmsInnerInventory.setCreateTime(new Date());
        wmsInnerInventory.setCreateUserId(sysUser.getUserId());
        wmsInnerInventory.setOrgId(sysUser.getOrganizationId());
        wmsInnerInventory.setModifiedTime(new Date());
        wmsInnerInventory.setModifiedUserId(sysUser.getUserId());
        wmsInnerInventory.setProductionDate(new Date());
        wmsInnerInventory.setRelevanceOrderCode(notOrderInStorage.getRelevanceOrderCode());
        wmsInnerInventory.setReceivingDate(new Date());
        //查询是否有相同库存
        int num = 0;
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("materialId", wmsInnerInventory.getMaterialId()).andEqualTo("warehouseId", wmsInnerInventory.getWarehouseId()).andEqualTo("storageId", wmsInnerInventory.getStorageId());
        criteria1.andEqualTo("jobStatus", (byte) 1);
        criteria1.andEqualTo("inventoryStatusId", wmsInnerInventory.getInventoryStatusId());
        criteria1.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(wmsInnerInventorys)){
            if(StringUtils.isEmpty(wmsInnerInventorys.getPackingQty()) || wmsInnerInventorys.getPackingQty().compareTo(BigDecimal.ZERO)==0){
                wmsInnerInventorys.setReceivingDate(wmsInnerInventory.getReceivingDate());
            }
            //原库存
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerInventory.getPackingQty()));
            wmsInnerInventorys.setWorkOrderCode(wmsInnerInventory.getWorkOrderCode());
            wmsInnerInventorys.setRelevanceOrderCode(wmsInnerInventory.getRelevanceOrderCode());
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num+= wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }else {
            num+=wmsInnerInventoryMapper.insertSelective(wmsInnerInventory);
        }
        //新增库存明细
        WmsInnerInventoryDet wmsInnerInventoryDet = new WmsInnerInventoryDet();
        BeanUtils.copyProperties(wmsInnerInventory,wmsInnerInventoryDet);
        wmsInnerInventoryDet.setMaterialQty(notOrderInStorage.getPackingQty());
        wmsInnerInventoryDet.setBarcode(notOrderInStorage.getBarcode());
        wmsInnerInventoryDet.setSalesBarcode(notOrderInStorage.getSalesBarcode());
        wmsInnerInventoryDet.setBarcodeStatus((byte)3);
        wmsInnerInventoryDet.setIfStockLock((byte)0);
        wmsInnerInventoryDet.setJobStatus((byte)2);
        wmsInnerInventoryDetMapper.insertSelective(wmsInnerInventoryDet);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerInventory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = wmsInnerInventoryMapper.insertUseGeneratedKeys(record);

        WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
        BeanUtils.copyProperties(record,wmsHtInnerInventory);
        wmsHtInnerInventoryMapper.insert(wmsHtInnerInventory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int update(WmsInnerInventory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
        BeanUtils.copyProperties(entity,wmsHtInnerInventory);
        wmsHtInnerInventoryMapper.insert(wmsHtInnerInventory);

        return wmsInnerInventoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<WmsHtInnerInventory> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
            BeanUtils.copyProperties(wmsInnerInventory,wmsHtInnerInventory);
            list.add(wmsHtInnerInventory);
        }

        wmsHtInnerInventoryMapper.insertList(list);
        return wmsInnerInventoryMapper.deleteByIds(ids);
    }
}
