package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.wms.inner.InitStockCheckBarCode;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.InitStockImport;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.mapper.*;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockService;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/01.
 */
@Service
@Slf4j
public class WmsInnerInitStockServiceImpl extends BaseService<WmsInnerInitStock> implements WmsInnerInitStockService {

    @Resource
    private WmsInnerInitStockMapper wmsInnerInitStockMapper;
    @Resource
    private WmsInnerInitStockDetMapper wmsInnerInitStockDetMapper;
    @Resource
    private WmsInnerInitStockBarcodeMapper wmsInnerInitStockBarcodeMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;

    @Override
    public List<WmsInnerInitStockDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInnerInitStockMapper.findList(map);
    }

    @Override
    public InitStockCheckBarCode checkBarCode(String barCode) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();


        //查询是否重复扫码
        Example example = new Example(WmsInnerInitStockBarcode.class);
        Example.Criteria criteria =example.createCriteria();

        InitStockCheckBarCode initStockCheckBarCode = new InitStockCheckBarCode();
        boolean isInPlantBarCode = true;
        if(barCode.length()<12){
            isInPlantBarCode = false;
           // throw new BizErrorException("条码错误");
        }
        //判断是否是厂内码3911
        initStockCheckBarCode.setBarCode(barCode);
        if (barCode.length() == 23){
            initStockCheckBarCode.setType((byte)1);
            initStockCheckBarCode.setInPlantBarcode(barCode);
            criteria.andEqualTo("inPlantBarcode",barCode);
            String code = barCode.substring(0,1);
            if(isInPlantBarCode && code.equals("9")){
                //梅州厂内码
                //首位转换 3 第五位替换成 0
                StringBuilder sb = new StringBuilder(barCode);
                sb.replace(0,1,"3");
                sb.replace(4,5,"0");
                barCode = sb.toString();
            }else if(isInPlantBarCode && code.equals("8")){
                //民权厂内码
                //首位转换成3
                StringBuilder sb = new StringBuilder(barCode);
                sb.replace(0,1,"3");
                barCode = sb.toString();
            }
        } else if (barCode.contains("391-")){
            //销售条码 返回 2
            initStockCheckBarCode.setType((byte)2);
            initStockCheckBarCode.setSalesBarcode(barCode);
            criteria.andEqualTo("salesBarcode",barCode);
        }else {
            initStockCheckBarCode.setType((byte)3);
            //客户条码匹配PQMS条码查询厂内码及对应物料
            SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
            searchMesSfcBarcodeProcess.setIsCustomerBarcode(barCode);
            ResponseEntity<List<MesSfcBarcodeProcessDto>> responseEntity = sfcFeignApi.findList(searchMesSfcBarcodeProcess);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if(responseEntity.getData().isEmpty()){
                //客户条码
                initStockCheckBarCode.setType((byte)4);
                initStockCheckBarCode.setClientBarcode(barCode);
            }else {
                initStockCheckBarCode.setType((byte)3);
                //三星厂内码
                initStockCheckBarCode.setInPlantBarcode(responseEntity.getData().get(0).getBarcode());
                criteria.andEqualTo("inPlantBarcode",initStockCheckBarCode.getInPlantBarcode());
                initStockCheckBarCode.setClientBarcode(responseEntity.getData().get(0).getCustomerBarcode());
                barCode = initStockCheckBarCode.getInPlantBarcode();
            }
            criteria.andEqualTo("clientBarcode1",barCode).orEqualTo("clientBarcode2",barCode).orEqualTo("clientBarcode3",barCode);
        }

        List<WmsInnerInitStockBarcode> wmsInnerInitStockBarcodes = wmsInnerInitStockBarcodeMapper.selectByExample(example);
        if (!wmsInnerInitStockBarcodes.isEmpty()){
            if(initStockCheckBarCode.getType()==4){
                for (WmsInnerInitStockBarcode wmsInnerInitStockBarcode : wmsInnerInitStockBarcodes) {
                    //判断是否GE/多美达
                    WmsInnerInitStockDet wmsInnerInitStockDet = wmsInnerInitStockDetMapper.selectByPrimaryKey(wmsInnerInitStockBarcode.getInitStockDetId());
                    if(wmsInnerInitStockDet.getProductType()==2){
                        //获取长度最
                        String clientCode = null;
                        if(wmsInnerInitStockBarcode.getClientBarcode1().length()<wmsInnerInitStockBarcode.getClientBarcode2().length() && wmsInnerInitStockBarcode.getClientBarcode1().length()<wmsInnerInitStockBarcode.getClientBarcode3().length()){
                            clientCode = wmsInnerInitStockBarcode.getClientBarcode1();
                        }else if(wmsInnerInitStockBarcode.getClientBarcode2().length()<wmsInnerInitStockBarcode.getClientBarcode1().length() && wmsInnerInitStockBarcode.getClientBarcode2().length()<wmsInnerInitStockBarcode.getClientBarcode3().length()){
                            clientCode = wmsInnerInitStockBarcode.getClientBarcode2();
                        }else{
                            clientCode = wmsInnerInitStockBarcode.getClientBarcode3();
                        }

                        if(StringUtils.isNotEmpty(clientCode) && clientCode.equals(initStockCheckBarCode.getClientBarcode())){
                            throw new BizErrorException("重复扫码");
                        }
                    }
                }
            }else {
                throw new BizErrorException("重复扫码");
            }
        }
        //厂内码截取12位 查询物料信息
        if(isInPlantBarCode && (StringUtils.isNotEmpty(initStockCheckBarCode.getInPlantBarcode())|| StringUtils.isNotEmpty(initStockCheckBarCode.getClientBarcode()))){
            if(StringUtils.isEmpty(initStockCheckBarCode.getInPlantBarcode())){
                initStockCheckBarCode.setInPlantBarcode(initStockCheckBarCode.getClientBarcode());
            }
            String materialCode = barCode.substring(0,12);
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setCodeQueryMark(1);
            ResponseEntity<List<BaseMaterial>> responseEntity = baseFeignApi.findList(searchBaseMaterial);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if(responseEntity.getData().isEmpty()){
                if(initStockCheckBarCode.getType()==4){
                    initStockCheckBarCode.setInPlantBarcode(null);
                }else {
                    throw new BizErrorException("条码："+initStockCheckBarCode.getBarCode()+"未查询到物料信息");
                }
            }else {
                if(initStockCheckBarCode.getType()==4){
                    //查询是否重复
                    example.clear();
                    criteria.andEqualTo("inPlantBarcode",barCode);
                    WmsInnerInitStockBarcode wmsInnerInitStockBarcode = wmsInnerInitStockBarcodeMapper.selectOneByExample(example);
                    if(StringUtils.isNotEmpty(wmsInnerInitStockBarcode)){
                        throw new BizErrorException("重复扫码");
                    }
                    initStockCheckBarCode.setClientBarcode(null);
                    initStockCheckBarCode.setType((byte)1);
                }
                initStockCheckBarCode.setMaterialId(responseEntity.getData().get(0).getMaterialId());
                initStockCheckBarCode.setMaterialCode(responseEntity.getData().get(0).getMaterialCode());
                initStockCheckBarCode.setMaterialDesc(responseEntity.getData().get(0).getMaterialDesc());
            }
        }

        return initStockCheckBarCode;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int finish(Long initStockId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerInitStock wmsInnerInitStock = wmsInnerInitStockMapper.selectByPrimaryKey(initStockId);
        Example example = new Example(WmsInnerInitStockDet.class);
        example.createCriteria().andEqualTo("initStockId",wmsInnerInitStock.getInitStockId());
        List<WmsInnerInitStockDet> wmsInnerInitStockDets = wmsInnerInitStockDetMapper.selectByExample(example);

        //查询库位
        SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
        searchBaseStorage.setStorageId(wmsInnerInitStock.getStorageId());
        ResponseEntity<List<BaseStorage>> responseEntity = baseFeignApi.findList(searchBaseStorage);
        if (responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        if(responseEntity.getData().isEmpty()){
            throw new BizErrorException("未获取到库位信息");
        }
        //查询库存状态
        SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
        searchBaseInventoryStatus.setWarehouseId(responseEntity.getData().get(0).getWarehouseId());
        searchBaseInventoryStatus.setInventoryStatusName("合格");
        searchBaseInventoryStatus.setNameQueryMark(1);
        ResponseEntity<List<BaseInventoryStatus>> rs = baseFeignApi.findList(searchBaseInventoryStatus);
        if (rs.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        if(rs.getData().isEmpty()){
            throw new BizErrorException("未获取到仓库库存状态");
        }

        for (WmsInnerInitStockDet innerInitStockDet : wmsInnerInitStockDets) {
            //查询条码
            example = new Example(WmsInnerInitStockBarcode.class);
            example.createCriteria().andEqualTo("initStockDetId",innerInitStockDet.getInitStockDetId());
            List<WmsInnerInitStockBarcode> wmsInnerInitStockBarcodes = wmsInnerInitStockBarcodeMapper.selectByExample(example);


            //添加库存明细
            for (WmsInnerInitStockBarcode wmsInnerInitStockBarcode : wmsInnerInitStockBarcodes) {
                example = new Example(WmsInnerInventoryDet.class);
                example.createCriteria().andEqualTo("barcode", wmsInnerInitStockBarcode.getInPlantBarcode()).andEqualTo("storageId", wmsInnerInitStock.getStorageId()).andEqualTo("materialId", innerInitStockDet.getMaterialId()).andEqualTo("orgId",sysUser.getOrganizationId());
                WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(wmsInnerInventoryDet)){
                    wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInventoryDet.setModifiedTime(new Date());
                }else {
                    wmsInnerInventoryDet = new WmsInnerInventoryDet();
                    wmsInnerInventoryDet.setBarcode(wmsInnerInitStockBarcode.getInPlantBarcode());
                    wmsInnerInventoryDet.setMaterialId(innerInitStockDet.getMaterialId());
                    wmsInnerInventoryDet.setStorageId(wmsInnerInitStock.getStorageId());
                    wmsInnerInventoryDet.setBarcodeStatus((byte)3);
                    wmsInnerInventoryDet.setMaterialQty(new BigDecimal(1));
                    wmsInnerInventoryDet.setCreateTime(new Date());
                    wmsInnerInventoryDet.setCreateUserId(sysUser.getUserId());
                    wmsInnerInventoryDet.setModifiedTime(new Date());
                    wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
                    wmsInnerInventoryDet.setInventoryStatusId(rs.getData().get(0).getInventoryStatusId());
                    wmsInnerInventoryDetMapper.insertSelective(wmsInnerInventoryDet);
                }
            }
            //添加库存
            example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("materialId", innerInitStockDet.getMaterialId()).andEqualTo("warehouseId", responseEntity.getData().get(0).getWarehouseId()).andEqualTo("storageId", wmsInnerInitStock.getStorageId());
            criteria1.andEqualTo("jobStatus", (byte) 1);
            criteria1.andEqualTo("inventoryStatusId", rs.getData().get(0).getInventoryStatusId());
            criteria1.andEqualTo("stockLock", 0).andEqualTo("qcLock", 0).andEqualTo("lockStatus", 0);
            criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
            WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
            BigDecimal qty = BigDecimal.ZERO;
            if (StringUtils.isEmpty(wmsInnerInventorys)) {
                //添加库存
                WmsInnerInventory inv = new WmsInnerInventory();
                inv.setStorageId(wmsInnerInitStock.getStorageId());
                inv.setWarehouseId(responseEntity.getData().get(0).getWarehouseId());
                inv.setPackingQty(innerInitStockDet.getStockQty());
                inv.setMaterialId(innerInitStockDet.getMaterialId());
                inv.setJobStatus((byte) 1);
                inv.setInventoryId(null);
                inv.setCreateUserId(sysUser.getUserId());
                inv.setCreateTime(new Date());
                inv.setOrgId(sysUser.getOrganizationId());
                inv.setModifiedTime(new Date());
                inv.setModifiedUserId(sysUser.getUserId());
                inv.setRelevanceOrderCode(wmsInnerInitStock.getInitStockOrderCode());
                inv.setJobOrderDetId(null);
                inv.setInventoryStatusId(rs.getData().get(0).getInventoryStatusId());
                wmsInnerInventoryMapper.insertSelective(inv);
                wmsInnerInventorys = new WmsInnerInventory();
                BeanUtil.copyProperties(inv,wmsInnerInventorys);
            } else {
                qty = wmsInnerInventorys.getPackingQty();
                //原库存
                wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(innerInitStockDet.getStockQty()));
                wmsInnerInventorys.setRelevanceOrderCode(wmsInnerInitStock.getInitStockOrderCode());
                wmsInnerInventorys.setModifiedTime(new Date());
                wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            }
            //记录库存日志
            InventoryLogUtil.initStockLog(wmsInnerInventorys,wmsInnerInitStock,innerInitStockDet,responseEntity.getData().get(0).getWarehouseId(),rs.getData().get(0).getInventoryStatusId(),qty,innerInitStockDet.getStockQty(),(byte)2,(byte)1);
        }
        wmsInnerInitStock.setModifiedUserId(sysUser.getUserId());
        wmsInnerInitStock.setModifiedTime(new Date());
        wmsInnerInitStock.setOrderStatus((byte)3);
        return wmsInnerInitStockMapper.updateByPrimaryKeySelective(wmsInnerInitStock);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> importExcel(List<InitStockImport> initStockImports) {
        SysUser sysUser =CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        int i = 0;
        for (InitStockImport initStockImport : initStockImports) {
            //查询库位id
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setStorageCode(initStockImport.getStorageCode());
            searchBaseStorage.setCodeQueryMark((byte)1);
            ResponseEntity<List<BaseStorage>> responseEntity = baseFeignApi.findList(searchBaseStorage);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if(responseEntity.getData().isEmpty()){
                fail.add(i++);
            }
            initStockImport.setStorageId(responseEntity.getData().get(0).getStorageId());

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(initStockImport.getMaterialCode());
            searchBaseMaterial.setCodeQueryMark(1);
            ResponseEntity<List<BaseMaterial>> rs = baseFeignApi.findList(searchBaseMaterial);
            if(rs.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
            if(rs.getData().isEmpty()){
                fail.add(i++);
            }
            initStockImport.setMaterialId(rs.getData().get(0).getMaterialId());
        }

        //按盘点类型分组
        HashMap<String,List<InitStockImport>> map = initStockImports.stream().collect(Collectors.groupingBy(InitStockImport::getInitStockType,HashMap::new, Collectors.toList()));
        Set<String> set = map.keySet();
        for (String s : set) {
            //按库位分组
            HashMap<String,List<InitStockImport>> strMap = map.get(s).stream().collect(Collectors.groupingBy(InitStockImport::getStorageCode,HashMap::new,Collectors.toList()));
            Set<String> st = strMap.keySet();
            for (String s1 : st) {
                //表头
                WmsInnerInitStock wmsInnerInitStock = new WmsInnerInitStock();
                wmsInnerInitStock.setInitStockOrderCode(CodeUtils.getId("IS"));
                wmsInnerInitStock.setStorageId(strMap.get(s1).get(0).getStorageId());
                wmsInnerInitStock.setInitStockType(Byte.parseByte(map.get(s).get(0).getInitStockType()));
                wmsInnerInitStock.setOrderStatus((byte)1);
                wmsInnerInitStock.setCreateTime(new Date());
                wmsInnerInitStock.setCreateUserId(sysUser.getUserId());
                wmsInnerInitStock.setModifiedTime(new Date());
                wmsInnerInitStock.setModifiedUserId(sysUser.getUserId());
                wmsInnerInitStock.setOrgId(sysUser.getOrganizationId());
                wmsInnerInitStockMapper.insertUseGeneratedKeys(wmsInnerInitStock);
                for (InitStockImport initStockImport : strMap.get(s1)) {
                    WmsInnerInitStockDet wmsInnerInitStockDet = new WmsInnerInitStockDet();
                    wmsInnerInitStockDet.setInitStockId(wmsInnerInitStock.getInitStockId());
                    wmsInnerInitStockDet.setMaterialId(initStockImport.getMaterialId());
                    wmsInnerInitStockDet.setPlanQty(initStockImport.getPlanQty());
                    wmsInnerInitStockDet.setCreateTime(new Date());
                    wmsInnerInitStockDet.setCreateUserId(sysUser.getUserId());
                    wmsInnerInitStockDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerInitStockDet.setModifiedTime(new Date());
                    success += wmsInnerInitStockDetMapper.insertSelective(wmsInnerInitStockDet);
                }
            }
        }
        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int deleteBarCode(Long initStockBarCodeId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerInitStockBarcode wmsInnerInitStockBarcode = wmsInnerInitStockBarcodeMapper.selectByPrimaryKey(initStockBarCodeId);
        WmsInnerInitStockDet wmsInnerInitStockDet = wmsInnerInitStockDetMapper.selectByPrimaryKey(wmsInnerInitStockBarcode.getInitStockDetId());
        wmsInnerInitStockDet.setStockQty(wmsInnerInitStockDet.getStockQty().subtract(BigDecimal.ONE));
        //差异量
        BigDecimal varQty = wmsInnerInitStockDet.getStockQty().subtract(wmsInnerInitStockDet.getPlanQty());
        int qty = varQty.intValue();
        if(varQty.signum()==-1){
            qty =  ~(varQty.intValue() - 1);
        }
        wmsInnerInitStockDet.setVarianceQty(new BigDecimal(qty));
        wmsInnerInitStockDet.setModifiedTime(new Date());
        wmsInnerInitStockDet.setModifiedUserId(sysUser.getUserId());
        wmsInnerInitStockDetMapper.updateByPrimaryKeySelective(wmsInnerInitStockDet);
        return wmsInnerInitStockBarcodeMapper.deleteByPrimaryKey(initStockBarCodeId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInnerInitStock record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        record.setInitStockOrderCode(CodeUtils.getId("IS"));
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        record.setOrderStatus((byte)1);
        int num = wmsInnerInitStockMapper.insertUseGeneratedKeys(record);
        for (WmsInnerInitStockDet wmsInnerInitStockDet : record.getWmsInnerInitStockDets()) {
//            if(record.getInitStockType()==1){
//                wmsInnerInitStockDet.setPlanQty(record.getTotalPlanQty());
//            }
            wmsInnerInitStockDet.setCreateTime(new Date());
            wmsInnerInitStockDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInitStockDet.setModifiedTime(new Date());
            wmsInnerInitStockDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerInitStockDet.setOrgId(sysUser.getOrganizationId());
            wmsInnerInitStockDet.setInitStockId(record.getInitStockId());
        }
        if(!StringUtils.isEmpty(record.getWmsInnerInitStockDets())){
            wmsInnerInitStockDetMapper.insertList(record.getWmsInnerInitStockDets());
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInnerInitStock entity) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(entity.getOrderStatus()>1){
            throw new BizErrorException("已盘点，无法修改");
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setOrgId(sysUser.getOrganizationId());
        Example example = new Example(WmsInnerInitStockDet.class);
        example.createCriteria().andEqualTo("initStockId",entity.getInitStockId());
        wmsInnerInitStockDetMapper.deleteByExample(example);
        for (WmsInnerInitStockDet wmsInnerInitStockDet : entity.getWmsInnerInitStockDets()) {
//            if(entity.getInitStockType()==1){
//                wmsInnerInitStockDet.setPlanQty(entity.getTotalPlanQty());
//            }
            wmsInnerInitStockDet.setCreateTime(new Date());
            wmsInnerInitStockDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInitStockDet.setModifiedTime(new Date());
            wmsInnerInitStockDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerInitStockDet.setOrgId(sysUser.getOrganizationId());
            wmsInnerInitStockDet.setInitStockId(entity.getInitStockId());
        }
        if(!StringUtils.isEmpty(entity.getWmsInnerInitStockDets())){
            wmsInnerInitStockDetMapper.insertList(entity.getWmsInnerInitStockDets());
        }
        return super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] id = ids.split(",");
        for (String s : id) {
            WmsInnerInitStock wmsInnerInitStock = wmsInnerInitStockMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInnerInitStock)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(wmsInnerInitStock.getOrderStatus()>1){
                throw new BizErrorException("已盘点，无法删除");
            }
            //查询明细
            Example example = new Example(WmsInnerInitStockDet.class);
            example.createCriteria().andEqualTo("initStockId",wmsInnerInitStock.getInitStockId());
            List<WmsInnerInitStockDet> list = wmsInnerInitStockDetMapper.selectByExample(example);

            //删除条码
            for (WmsInnerInitStockDet wmsInnerInitStockDet : list) {
                example = new Example(WmsInnerInitStockBarcode.class);
                example.createCriteria().andEqualTo("initStockDetId",wmsInnerInitStockDet.getInitStockDetId());
                wmsInnerInitStockBarcodeMapper.deleteByExample(example);
                wmsInnerInitStockDetMapper.deleteByPrimaryKey(wmsInnerInitStockDet.getInitStockDetId());
            }
        }
        return super.batchDelete(ids);
    }
}
