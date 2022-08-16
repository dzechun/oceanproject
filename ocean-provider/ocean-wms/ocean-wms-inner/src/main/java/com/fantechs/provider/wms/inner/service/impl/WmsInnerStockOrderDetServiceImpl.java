package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerStockOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStockOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */
@Service
public class WmsInnerStockOrderDetServiceImpl extends BaseService<WmsInnerStockOrderDet> implements WmsInnerStockOrderDetService {

    @Resource
    private WmsInnerStockOrderMapper wmsInnerStockOrderMapper;
    @Resource
    private WmsInnerStockOrderDetMapper wmsInventoryVerificationDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Override
    public List<WmsInnerStockOrderDetDto> findList(Map<String, Object> map) {
        return wmsInventoryVerificationDetMapper.findList(map);
    }

    @Override
    public Map<String, Object> importExcel(Long stockOrderId, List<WmsInnerStockOrderImport> wmsInnerStockOrderImports) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerStockOrder wmsInnerStockOrder = wmsInnerStockOrderMapper.selectByPrimaryKey(stockOrderId);
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        for (int i = 0; i < wmsInnerStockOrderImports.size(); i++) {

            WmsInnerStockOrderDet wmsInnerStockOrderDet = new WmsInnerStockOrderDet();
            BeanUtils.copyProperties(wmsInnerStockOrderImports.get(i),wmsInnerStockOrderDet);
            wmsInnerStockOrderDet.setStockOrderId(stockOrderId);
            wmsInnerStockOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInnerStockOrderDet.setCreateTime(new Date());
            wmsInnerStockOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerStockOrderDet.setModifiedTime(new Date());
            wmsInnerStockOrderDet.setOrganizationId(sysUser.getOrganizationId());
            wmsInnerStockOrderDet.setStockUserId(sysUser.getUserId());
            wmsInnerStockOrderDet.setIfRegister((byte)1);
            wmsInnerStockOrderDet.setOriginalQty(BigDecimal.ZERO);
            if(StringUtils.isEmpty(wmsInnerStockOrderImports.get(i).getStockQty())){
                wmsInnerStockOrderDet.setStockQty(BigDecimal.ZERO);
            }
            wmsInnerStockOrderDet.setVarianceQty(wmsInnerStockOrderDet.getStockQty().subtract(wmsInnerStockOrderDet.getOriginalQty()));

            //获取库位
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setCodeQueryMark((byte)1);
            searchBaseStorage.setWarehouseId(wmsInnerStockOrder.getWarehouseId());
            searchBaseStorage.setStorageCode(wmsInnerStockOrderImports.get(i).getStorageCode());
            List<BaseStorage> baseStorageList = baseFeignApi.findList(searchBaseStorage).getData();
            if(baseStorageList.isEmpty()){
                fail.add(i+1);
                throw new BizErrorException(ErrorCodeEnum.GL9999404,"当前仓库没有"+searchBaseStorage.getStorageCode()+"库位,不允许导入");
            }
            wmsInnerStockOrderDet.setStorageId(baseStorageList.get(0).getStorageId());

            //获取物料
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setCodeQueryMark(1);
            searchBaseMaterial.setMaterialCode(wmsInnerStockOrderImports.get(i).getMaterialCode());
            List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
            if(baseMaterialList.isEmpty()){
                fail.add(i+1);
                throw new BizErrorException(ErrorCodeEnum.GL9999404,"未维护物料"+searchBaseMaterial.getMaterialCode()+"信息，不允许导入");
            }
            wmsInnerStockOrderDet.setMaterialId(baseMaterialList.get(0).getMaterialId());

            //库存状态
            SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
            searchBaseInventoryStatus.setWarehouseId(wmsInnerStockOrder.getWarehouseId());
            searchBaseInventoryStatus.setInventoryStatusName(wmsInnerStockOrderImports.get(i).getInventoryStatusName());
            searchBaseInventoryStatus.setNameQueryMark(1);
            List<BaseInventoryStatus> baseInventoryStatusList = baseFeignApi.findList(searchBaseInventoryStatus).getData();
            if(baseInventoryStatusList.isEmpty()){
                fail.add(i+1);
                throw new BizErrorException(ErrorCodeEnum.GL9999404,"当前仓库未维护"+searchBaseInventoryStatus.getInventoryStatusName()+"库存状态，不允许导入");
            }
            wmsInnerStockOrderDet.setInventoryStatusId(baseInventoryStatusList.get(0).getInventoryStatusId());

            //供应商
            if(StringUtils.isNotEmpty(wmsInnerStockOrderImports.get(i).getSupplierName())){
                SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
                searchBaseSupplier.setSupplierName(wmsInnerStockOrderImports.get(i).getSupplierName());
                searchBaseSupplier.setCodeQueryMark((byte)1);
                searchBaseSupplier.setSupplierType((byte)1);
                List<BaseSupplier> baseSupplierList = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
                if(baseSupplierList.isEmpty()){
                    fail.add(i+1);
                    throw new BizErrorException(ErrorCodeEnum.GL9999404,"无供应商信息，不允许导入");
                }
                wmsInnerStockOrderDet.setSupplierId(baseSupplierList.get(0).getSupplierId());
            }

            //盘点类型 a 盘点类型为“货品”时，只能增补盘点单已添加的货品，未添加的货品不允许添加；
            //
            //                        b 盘点类型为“库位”时，只能增补盘点单已添加的库位，，未添加的库位不允许添加；
            //
            //                        c 盘点类型为“全盘”时，不做限制。

            Example example = new Example(WmsInnerStockOrderDet.class);
            example.createCriteria().andEqualTo("stockOrderId",wmsInnerStockOrder.getStockOrderId());
            List<WmsInnerStockOrderDet> wmsInnerStockOrderDets = wmsInventoryVerificationDetMapper.selectByExample(example);

            //库位/货品类型
            if((wmsInnerStockOrder.getStockType()==1 && wmsInnerStockOrderDets.stream().filter(li-> Objects.equals(li.getStorageId(), wmsInnerStockOrderDet.getStorageId())).collect(Collectors.toList()).size()>0)||
                    (wmsInnerStockOrder.getStockType()==2 && wmsInnerStockOrderDets.stream().filter(li-> Objects.equals(li.getMaterialId(), wmsInnerStockOrderDet.getMaterialId())).collect(Collectors.toList()).size()>0)||
                    wmsInnerStockOrder.getStockType()==3){
                success+=wmsInventoryVerificationDetMapper.insertSelective(wmsInnerStockOrderDet);
            }
        }
        if(success>0){
            wmsInnerStockOrder.setOrderStatus((byte)3);
            wmsInnerStockOrder.setModifiedTime(new Date());
            wmsInnerStockOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerStockOrderMapper.updateByPrimaryKeySelective(wmsInnerStockOrder);
        }
        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            WmsInnerStockOrderDet wmsInnerStockOrderDet = wmsInventoryVerificationDetMapper.selectByPrimaryKey(id);

            WmsInnerStockOrder wmsInnerStockOrder = wmsInnerStockOrderMapper.selectByPrimaryKey(wmsInnerStockOrderDet.getStockOrderId());
            //解锁库存盘点锁
            //解锁库存
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",wmsInnerStockOrder.getWarehouseId()).andEqualTo("storageId",wmsInnerStockOrderDet.getStorageId())
                    .andEqualTo("materialId",wmsInnerStockOrderDet.getMaterialId())
                    .andEqualTo("batchCode",wmsInnerStockOrderDet.getBatchCode())
                    .andEqualTo("stockLock",1)
                    .andEqualTo("orgId",sysUser.getOrganizationId())
                    .andEqualTo("jobStatus",1).andEqualTo("packingQty",wmsInnerStockOrderDet.getOriginalQty()).andEqualTo("relevanceOrderCode",wmsInnerStockOrder.getStockOrderCode());
            List<WmsInnerInventory> wmsInnerInventories = wmsInnerInventoryMapper.selectByExample(example);
            for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                wmsInnerInventory.setStockLock((byte)0);
                wmsInnerInventory.setRelevanceOrderCode(wmsInnerStockOrder.getStockOrderCode());
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }
        }
        return super.batchDelete(ids);
    }
}
