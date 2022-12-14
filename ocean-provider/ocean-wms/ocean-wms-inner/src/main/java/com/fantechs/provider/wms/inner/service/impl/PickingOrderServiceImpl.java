package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.StorageRuleInventry;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcKeyPartRelevance;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import com.fantechs.provider.wms.inner.util.InBarcodeUtil;
import com.fantechs.provider.wms.inner.util.InventoryLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
@Slf4j
public class PickingOrderServiceImpl implements PickingOrderService {

    @Resource
    private WmsInnerJobOrderMapper wmsInnerJobOrderMapper;
    @Resource
    private WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private WmsInnerInventoryService wmsInnerInventoryService;

    private String REDIS_KEY = "PICKINGID:";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WmsInnerJobOrderDet scanAffirmQty(String barCode,String storageCode,BigDecimal qty,Long jobOrderDetId) {
        SysUser sysUser = currentUser();

        if(StringUtils.isEmpty(qty)){
            throw new BizErrorException("????????????????????????1");
        }
        //??????????????????????????????id
        ResponseEntity<List<BaseStorage>> list = baseFeignApi.findList(SearchBaseStorage.builder()
                .storageCode(storageCode)
                .codeQueryMark((byte)1)
                .build());
        if(StringUtils.isEmpty(list.getData())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"??????????????????");
        }
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==1){
            throw new BizErrorException("????????????????????????????????????");
        }
        //BaseStorage baseStorage = list.getData().get(0);

        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
        WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);
        int num = 0;
        if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==-1){
            WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet,wms);
            wms.setJobOrderDetId(null);
            wms.setPlanQty(qty);
            wms.setDistributionQty(qty);
            wms.setActualQty(qty);
            wms.setOrderStatus((byte)5);
            wms.setWorkStartTime(new Date());
            num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wms.getJobOrderDetId());

            wmsInnerJobOrderDet.setOrderStatus((byte)4);
            wmsInnerJobOrderDet.setInStorageId(null);
            wmsInnerJobOrderDet.setPlanQty(wmsInnerJobOrderDet.getPlanQty().subtract(wms.getPlanQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
            wmsInnerJobOrderDet.setActualQty(null);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        }else if(wmsInnerJobOrderDet.getActualQty().add(qty).compareTo(wmsInnerJobOrderDet.getDistributionQty())==0){
            //????????????
            wmsInnerJobOrderDet.setActualQty(qty);
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setModifiedTime(new Date());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        }


        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrderDet.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        List<WmsInnerJobOrderDetDto> wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        //????????????
        num = this.Inventory(oldDto,wmsInnerJobOrderDetDto.get(0));
        //??????????????????
        if(StringUtils.isNotEmpty(barCode)){
            String[] code = barCode.split(",");
            for (String s : code) {
                num +=this.addInventoryDet(s,wmsInnerJobOrderDto.getJobOrderCode(),wmsInnerJobOrderDetDto.get(0));
            }
        }
        WmsInnerJobOrderDet wms= new WmsInnerJobOrderDet();
        wms.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
        int count = wmsInnerJobOrderDetMapper.selectCount(wms);
        wms.setOrderStatus((byte)5);
        int oCount = wmsInnerJobOrderDetMapper.selectCount(wms);


        if(oCount==count){
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)5);
            if(StringUtils.isEmpty(ws.getActualQty())){
                ws.setActualQty(new BigDecimal("0.00"));
            }
            ws.setActualQty(ws.getActualQty().add(qty));
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            ws.setWorkEndtTime(new Date());
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
        }else{
            WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
            ws.setOrderStatus((byte)4);
            if(StringUtils.isEmpty(ws.getActualQty())){
                ws.setActualQty(new BigDecimal("0.00"));
            }
            ws.setActualQty(ws.getActualQty().add(qty));
            ws.setModifiedUserId(sysUser.getUserId());
            ws.setModifiedTime(new Date());
            if(StringUtils.isEmpty(wmsInnerJobOrderDto.getWorkStartTime())){
                ws.setWorkStartTime(new Date());
            }
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
        }

        //???????????????????????????
        this.writeDeliveryOrderQty(wmsInnerJobOrderDetDto.get(0));


        return wmsInnerJobOrderDet;
    }

    /**
     * PDA????????????????????????????????????
     * @return
     */
    private int addInventoryDet(String barcode,String jobOrderCode,WmsInnerJobOrderDet wmsInnerJobOrderDet){
        //???????????????????????????
        String factoryBarcode=this.getFactoryBarcode(barcode);
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode",factoryBarcode).andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                .andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("barcodeStatus",3)
                .andEqualTo("orgId",wmsInnerJobOrderDet.getOrgId());
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventoryDet)){
            throw new BizErrorException("??????????????????");
        }
        wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerInventoryDet.setDeliveryOrderCode(jobOrderCode);
        wmsInnerInventoryDet.setDeliverDate(new Date());
        wmsInnerInventoryDet.setBarcodeStatus((byte)4);
        return wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);
    }

    /**
     * ????????????
     * @param barCode
     * @return ????????????
     */
    @Override
    public Map<String ,Object> checkBarcode(String barCode,Long jobOrderDetId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        Map<String ,Object> map  = new HashMap<>();

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
        List<BaseMaterial> baseMaterialList = baseFeignApi.findList(searchBaseMaterial).getData();
        String materialCode = null;
        if(StringUtils.isNotEmpty(baseMaterialList)){
            materialCode = baseMaterialList.get(0).getMaterialCode();
        }
        if(StringUtils.isNotEmpty(materialCode) && materialCode.equals(barCode)){
            map.put("SN","false");
            return map;
        }else{
            //??????????????????????????????
//            WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = outFeignApi.detail(wmsInnerJobOrderDet.getSourceDetId()).getData();
            String factoryBarcode = this.getFactoryBarcode(barCode);
            BigDecimal qty = InBarcodeUtil.pickCheckBarCode(wmsInnerJobOrderDet.getInventoryStatusId(),wmsInnerJobOrderDet.getMaterialId(),factoryBarcode);
            map.put("SN","true");
            map.put("qty",qty);
        }
        return map;
    }

    /**
     * ????????????
     * @param ids
     * @return
     */
    //@Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistributionOld(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==(byte)3){
                throw new BizErrorException("?????????????????????");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId()).andEqualTo("orderStatus",1);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            //????????????????????????
            int success = 0;
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //????????????
                BaseStorageRule baseStorageRule = new BaseStorageRule();
                baseStorageRule.setMaterialId(wms.getMaterialId());
                baseStorageRule.setQty(wms.getPlanQty());
                baseStorageRule.setSalesBarcode(wms.getOption1());
                //baseStorageRule.setSalesBarcode(wms.getOption2());
                baseStorageRule.setPoCode(wms.getOption2());
                ResponseEntity<Long> responseEntity = baseFeignApi.outRule(baseStorageRule);
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
                }
                if(StringUtils.isNotEmpty(responseEntity.getData())){
                    Long storageId = responseEntity.getData();
                    wms.setOutStorageId(storageId);
                    wms.setDistributionQty(wms.getPlanQty());
                    wms.setOrderStatus((byte)3);
                    wms.setModifiedUserId(sysUser.getUserId());
                    wms.setModifiedTime(new Date());

                    //???????????? 2022-03-22
                    num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wms);
                    //????????????
                    num += this.DistributionInventory(wmsInnerJobOrder, wms,1,null);

                    success++;
                }



//                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//                List<WmsInnerInventory> wmsInnerInventories = OutInventoryRule.jobMainRule(wmsInnerJobOrder.getWarehouseId(),wms.getMaterialId(),StringUtils.isNotEmpty(wms.getBatchCode())?wms.getBatchCode():null,StringUtils.isNotEmpty(wms.getProductionDate())?sf.format(wms.getProductionDate()):null,StringUtils.isNotEmpty(wms.getInventoryStatusId())?wms.getInventoryStatusId():null);
//                if(StringUtils.isEmpty(wmsInnerInventories) || wmsInnerInventories.size()<1){
//                    throw new BizErrorException("??????????????????");
//                }
//                BigDecimal playQty = wms.getPlanQty();
//                for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
//                    if(playQty.compareTo(BigDecimal.ZERO)==1){
//                        if(wmsInnerInventory.getPackingQty().compareTo(playQty)>-1 && wms.getPlanQty().compareTo(playQty)==0){
//                            num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
//                                    .jobOrderDetId(wms.getJobOrderDetId())
//                                    .outStorageId(wmsInnerInventory.getStorageId())
//                                    .distributionQty(wms.getPlanQty())
//                                    .modifiedUserId(sysUser.getUserId())
//                                    .modifiedTime(new Date())
//                                    .orderStatus((byte)3)
//                                    .build());
//                            playQty = playQty.subtract(wms.getPlanQty());
//                            wms.setDistributionQty(wms.getPlanQty());
//                            wms.setOutStorageId(wmsInnerInventory.getStorageId());
//
//                        }else{
//                            if(wms.getPlanQty().compareTo(playQty)==0) {
//                                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
//                                        .jobOrderDetId(wms.getJobOrderDetId())
//                                        .outStorageId(wmsInnerInventory.getStorageId())
//                                        .planQty(wmsInnerInventory.getPackingQty())
//                                        .distributionQty(wmsInnerInventory.getPackingQty())
//                                        .modifiedUserId(sysUser.getUserId())
//                                        .modifiedTime(new Date())
//                                        .orderStatus((byte) 3)
//                                        .build());
//                                playQty = playQty.subtract(wmsInnerInventory.getPackingQty());
//                                wms.setOutStorageId(wmsInnerInventory.getStorageId());
//                                wms.setDistributionQty(wmsInnerInventory.getPackingQty());
//                                //????????????
//                                num += this.DistributionInventory(wmsInnerJobOrder, wms,2,wmsInnerInventory);
//                            }else{
//                                if(wmsInnerInventory.getPackingQty().compareTo(playQty)>-1){
//                                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
//                                    BeanUtil.copyProperties(wms,wmsInnerJobOrderDet);
//                                    wmsInnerJobOrderDet.setJobOrderDetId(null);
//                                    wmsInnerJobOrderDet.setPlanQty(playQty);
//                                    wmsInnerJobOrderDet.setDistributionQty(playQty);
//                                    num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
//                                    playQty = BigDecimal.ZERO;
//
//                                    //????????????
//                                    num += this.DistributionInventory(wmsInnerJobOrder, wmsInnerJobOrderDet,2,wmsInnerInventory);
//                                }else {
//                                    WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
//                                    BeanUtil.copyProperties(wms,wmsInnerJobOrderDet);
//                                    wmsInnerJobOrderDet.setJobOrderDetId(null);
//                                    wmsInnerJobOrderDet.setPlanQty(wmsInnerInventory.getPackingQty());
//                                    wmsInnerJobOrderDet.setDistributionQty(wmsInnerInventory.getPackingQty());
//                                    num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
//                                    playQty = playQty.subtract(wmsInnerInventory.getPackingQty());
//
//                                    //????????????
//                                    num += this.DistributionInventory(wmsInnerJobOrder, wmsInnerJobOrderDet,2,wmsInnerInventory);
//                                }
//                            }
//                        }
//                    }
//                }
                //???????????????1
                //baseFeignApi.minusSurplusCanPutSalver(wms.getInStorageId(),1);
//                WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wms.getJobOrderDetId());
            }
            if(success==0){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"????????????????????????");
            }else if(success==list.size()){
                wmsInnerJobOrder.setOrderStatus((byte)3);
            }else {
                wmsInnerJobOrder.setOrderStatus((byte)2);
            }
//            wmsInnerJobOrder.setOrderStatus((byte)3);
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * ????????????
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int autoDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        log.info("============= ids" + JSON.toJSONString(arrId));
        int num = 1;
        for (String id : arrId) {
            Boolean isFlat=false;
            Long storageId=null;
            List<Long> materialIdList=new ArrayList<>();
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if(wmsInnerJobOrder.getOrderStatus()==(byte)3){
                throw new BizErrorException("?????????????????????");
            }
            log.info("============= ?????????????????? ======================");
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId())
                    .andEqualTo("orderStatus",1);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            log.info("============= ?????????????????? ======================");
            //??????????????????????????????
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                Long materialId=wmsInnerJobOrderDet.getMaterialId();
                if(!materialIdList.contains(materialId)){
                    materialIdList.add(materialId);
                }
            }
            Map<Long, List<StorageRuleInventry>> listMap = new HashMap<>();
            for (Long longId : materialIdList) {
                List<StorageRuleInventry> inventryDetList=new ArrayList<>();
                //??????????????????
                StorageRuleInventry storageRuleInventry=new StorageRuleInventry();
                SearchWmsInnerInventory searchWmsInnerInventory=new SearchWmsInnerInventory();
                searchWmsInnerInventory.setMaterialId(longId);
                searchWmsInnerInventory.setLockStatus((byte)0);
                searchWmsInnerInventory.setStockLock((byte)0);
                searchWmsInnerInventory.setQcLock((byte)0);
                searchWmsInnerInventory.setJobStatus((byte)1);
                searchWmsInnerInventory.setStorageCode("Z-SX");
                searchWmsInnerInventory.setInventoryStatusName("??????");
                List<WmsInnerInventoryDto> innerInventoryList=wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
                log.info("============= ?????????????????????????????? " + JSON.toJSONString(innerInventoryList));

                if(StringUtils.isNotEmpty(innerInventoryList) && innerInventoryList.size()>0){
                    storageRuleInventry.setMaterialId(longId);
                    storageRuleInventry.setStorageId(innerInventoryList.get(0).getStorageId());
                    storageRuleInventry.setMaterialQty(innerInventoryList.get(0).getPackingQty());
                    inventryDetList.add(storageRuleInventry);
                }

                //??????????????????
                BaseStorageRule baseStorageRule = new BaseStorageRule();
                baseStorageRule.setMaterialId(longId);
                baseStorageRule.setQty(BigDecimal.ZERO);
                ResponseEntity<List<StorageRuleInventry>> responseEntity = baseFeignApi.returnOutStorage(baseStorageRule);
                List<StorageRuleInventry> detList=responseEntity.getData();
                if(StringUtils.isNotEmpty(detList) && detList.size()>0){
                    for (StorageRuleInventry ruleInventry : detList) {
                        inventryDetList.add(ruleInventry);
                    }
                }
                if(StringUtils.isNotEmpty(inventryDetList) && inventryDetList.size()>0){
                    //????????????????????????????????? ?????????????????????????????????
                    for (StorageRuleInventry ruleInventry : inventryDetList) {
                        Long storageIdS=ruleInventry.getStorageId();
                        Long materialIdS=ruleInventry.getMaterialId();

                        BigDecimal totalQty=null;

                        Example exampleInventory = new Example(WmsInnerInventory.class);
                        Example.Criteria criteriaInventory = exampleInventory.createCriteria();
                        criteriaInventory.andEqualTo("materialId",materialIdS);
                        criteriaInventory.andEqualTo("jobStatus",(byte)1);
                        criteriaInventory.andEqualTo("storageId",storageIdS).andGreaterThan("packingQty",0).andEqualTo("orgId",sysUser.getOrganizationId());
                        criteriaInventory.andEqualTo("stockLock",0).andEqualTo("qcLock",0).andEqualTo("lockStatus",0);
                        //inventoryStatusId: 137, inventoryStatusCode: null, inventoryStatusName: "??????"
                        criteriaInventory.andEqualTo("inventoryStatusId",137);
                        List<WmsInnerInventory> wmsInnerInventoryList = wmsInnerInventoryMapper.selectByExample(exampleInventory);
                        if(StringUtils.isNotEmpty(wmsInnerInventoryList) && wmsInnerInventoryList.size()>0) {
                            totalQty = wmsInnerInventoryList.stream().map(WmsInnerInventory::getPackingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        }
                        if(StringUtils.isNotEmpty(totalQty) && totalQty.compareTo(BigDecimal.ZERO)==1){
                            ruleInventry.setMaterialQty(totalQty);
                        }
                        else {
                            ruleInventry.setMaterialQty(BigDecimal.ZERO);
                        }
                    }
                    //List<WmsInnerMaterialBarcodeDto> barcodeListCarton = barcodeDtos.stream().filter(u -> ((StringUtils.isEmpty(u.getBarcode())?"":u.getBarcode())=="")).collect(Collectors.toList());
                    List<StorageRuleInventry> inventryDetListOK=inventryDetList.stream().filter(u -> ((u.getMaterialQty().compareTo(BigDecimal.ZERO)==1))).collect(Collectors.toList());
                    listMap.put(longId, inventryDetListOK);
                }
            }

            log.info("============= ???????????????????????? ======================");

            if(StringUtils.isEmpty(listMap)){
                log.info("============= ??????????????????????????? ======================");
                continue;
            }

            log.info("============= ????????????????????????" + JSON.toJSONString(listMap));

            //????????????????????????
            int success = 0;
            for (WmsInnerJobOrderDet wms : list) {
                if(StringUtils.isEmpty(wms)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                BigDecimal planQty=wms.getPlanQty();
                Long materialId=wms.getMaterialId();
                List<StorageRuleInventry> inventryList=listMap.get(materialId);
                if(StringUtils.isEmpty(inventryList) || inventryList.size()<=0){
                    continue;
                }
                for (StorageRuleInventry storageRuleInventry : inventryList) {
                    BigDecimal packingQty=storageRuleInventry.getMaterialQty();
                    if(packingQty.compareTo(new BigDecimal(0))==1) {
                        storageId=storageRuleInventry.getStorageId();
                        if (planQty.compareTo(packingQty) == 1) {
                            //????????????
                            WmsInnerJobOrderDet newDet = new WmsInnerJobOrderDet();
                            BeanUtil.copyProperties(wms,newDet);
                            newDet.setJobOrderDetId(null);
                            newDet.setPlanQty(packingQty);
                            newDet.setDistributionQty(packingQty);
                            newDet.setOutStorageId(storageRuleInventry.getStorageId());
                            newDet.setOrderStatus((byte)3);
                            num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(newDet);

                            planQty=planQty.subtract(packingQty);
                            storageRuleInventry.setMaterialQty(new BigDecimal(0));

                            isFlat=true;

                            //???????????? ???????????????
                            //num += this.DistributionInventory(wmsInnerJobOrder, newDet,1,null);
                        }
                        else {
                            //?????????
                            wms.setPlanQty(planQty);
                            wms.setDistributionQty(planQty);
                            wms.setOutStorageId(storageRuleInventry.getStorageId());
                            wms.setOrderStatus((byte)3);
                            wms.setModifiedUserId(sysUser.getUserId());
                            wms.setModifiedTime(new Date());
                            num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wms);

                            storageRuleInventry.setMaterialQty(packingQty.subtract(planQty));
                            planQty=new BigDecimal(0);

                            log.info("============= ????????? ??????????????????" + JSON.toJSONString(wms));

                            isFlat=true;

                            //???????????? ???????????????
                            //num += this.DistributionInventory(wmsInnerJobOrder, wms,1,null);
                        }

                        if(planQty.compareTo(new BigDecimal(0))<=0){
                            success++;
                            break;
                        }

                    }
                }

                //????????????????????? ?????????????????????0 ?????????????????????
                if(planQty.compareTo(new BigDecimal(0))==1){
                    wms.setPlanQty(planQty);
                    wms.setDistributionQty(new BigDecimal(0));
                    wms.setOrderStatus((byte)1);
                    wms.setModifiedUserId(sysUser.getUserId());
                    wms.setModifiedTime(new Date());
                    num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wms);
                }

            }
            if(success==list.size()){
                wmsInnerJobOrder.setOrderStatus((byte)3);
            }else if(isFlat) {
                wmsInnerJobOrder.setOrderStatus((byte)2);
            }

            if(StringUtils.isNotEmpty(storageId)){
                SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
                searchBaseStorage.setStorageId(storageId);
                List<BaseStorage> storageList=baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isNotEmpty(storageList) && storageList.size()>0){
                    wmsInnerJobOrder.setWorkingAreaId(storageList.get(0).getWorkingAreaId());
                }
            }
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    /**
     * ????????????
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int handDistribution(List<WmsInnerJobOrderDet> list) {
        SysUser sysUser = currentUser();
        int num=0;
        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : list) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderId());
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) && wmsInPutawayOrderDet.getDistributionQty().doubleValue()>wmsInPutawayOrderDet.getPlanQty().doubleValue()){
                throw new BizErrorException("????????????????????????????????????");
            }
            WmsInnerJobOrderDet ss = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderDetId());
            if(ss.getOrderStatus()>2){
                throw new BizErrorException("?????????????????????");
            }
            WmsInnerJobOrderDet ws = new WmsInnerJobOrderDet();
            if(StringUtils.isEmpty(wmsInPutawayOrderDet.getDistributionQty()) || wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==-1){
                //?????????
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty());
                wms.setOrderStatus((byte)3);
                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                ws = wms;

                wmsInPutawayOrderDet.setOrderStatus((byte)1);
                wmsInPutawayOrderDet.setDistributionQty(null);
                wmsInPutawayOrderDet.setOutStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(new BigDecimal(wmsInPutawayOrderDet.getPlanQty().doubleValue()-wms.getDistributionQty().doubleValue()));
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //????????????
                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                ws = wmsInPutawayOrderDet;
            }
            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            List<WmsInnerJobOrderDetDto> dto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);

            //????????????
            num += this.DistributionInventory(wmsInnerJobOrder, ws,1,null);

            if(StringUtils.isEmpty(dto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Long workingAreaId=null;
            if(StringUtils.isNotEmpty(ws.getOutStorageId())){
                SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
                searchBaseStorage.setStorageId(ws.getOutStorageId());
                List<BaseStorage> storageList=baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isNotEmpty(storageList) && storageList.size()>0){
                    workingAreaId=storageList.get(0).getWorkingAreaId();
                }
            }

            if(dto.stream().filter(li->li.getOrderStatus()==(byte)3).collect(Collectors.toList()).size()==dto.size()){
                //??????????????????
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)3)
                        .workingAreaId(workingAreaId)
                        .build());
            }else{
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .jobOrderId(wmsInPutawayOrderDet.getJobOrderId())
                        .orderStatus((byte)2)
                        .workingAreaId(workingAreaId)
                        .build());
            }
        }
        return num;
    }

    /**
     * ????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int cancelDistribution(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String s : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(wmsInnerJobOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            if(wmsInnerJobOrder.getOrderTypeId()==8){
                throw new BizErrorException("?????????????????????????????????");
            }
            if(wmsInnerJobOrder.getOrderStatus()==(byte)4 || wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("??????????????????????????????");
            }
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",s);
            List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
            Map<Long,List<WmsInnerJobOrderDet>> map = new HashMap<>();
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : list) {
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)4){
                    throw new BizErrorException("??????????????? ????????????");
                }
                if(map.containsKey(wmsInnerJobOrderDet.getMaterialId())){
                    List<WmsInnerJobOrderDet> nm = new ArrayList<>();
                    for (WmsInnerJobOrderDet innerJobOrderDet : map.get(wmsInnerJobOrderDet.getMaterialId())) {
                        innerJobOrderDet.setPlanQty(innerJobOrderDet.getPlanQty().add(wmsInnerJobOrderDet.getPlanQty()));
                        nm.add(innerJobOrderDet);
                    }
                    map.put(wmsInnerJobOrderDet.getMaterialId(),nm);
                }else{
                    List<WmsInnerJobOrderDet> list1 = new ArrayList<>();
                    list1.add(wmsInnerJobOrderDet);
                    map.put(wmsInnerJobOrderDet.getMaterialId(),list1);
                }

                //??????????????????
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)3){
                    int res = this.reconver(wmsInnerJobOrderDet.getJobOrderDetId());
                }
                //??????????????????
                Example example1 = new Example(WmsInnerInventory.class);
                example1.createCriteria().andEqualTo("jobOrderDetId",wmsInnerJobOrderDet.getJobOrderDetId()).andEqualTo("jobStatus",(byte)2).andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode());
                wmsInnerInventoryMapper.deleteByExample(example1);
            }
            wmsInnerJobOrderDetMapper.deleteByExample(example);
            for (List<WmsInnerJobOrderDet> value : map.values()) {
                for (WmsInnerJobOrderDet wmsInnerJobOrderDet : value) {
                    wmsInnerJobOrderDet.setDistributionQty(null);
                    wmsInnerJobOrderDet.setOutStorageId(null);
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    wmsInnerJobOrderDet.setModifiedTime(new Date());
                    wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
                    wmsInnerJobOrderDet.setOrderStatus((byte)1);
                    num +=wmsInnerJobOrderDetMapper.insertSelective(wmsInnerJobOrderDet);
                }
            }
            wmsInnerJobOrder.setOrderStatus((byte)1);
            num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);
        }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int allReceiving(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        int num = 0;
        for (String id : arrId) {
            WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(id);
            if (wmsInnerJobOrder.getOrderStatus()<(byte) 3) {
                throw new BizErrorException("???????????????,????????????");
            }
            double total = 0.00;
            if (wmsInnerJobOrder.getOrderStatus() == (byte) 5) {
                throw new BizErrorException("?????????????????????");
            }
            Long jobOrderDetId = null;
            Example example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = wmsInnerJobOrderDetMapper.selectByExample(example);


            //???????????????????????????
            if(wmsInnerJobOrder.getOrderTypeId()==8){
                wmsInnerJobOrder.setModifiedTime(new Date());
                wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
                wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                return this.PickingConfirmation(wmsInnerJobOrder,1);

            }


            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : wmsInnerJobOrderDets) {
                if(wmsInnerJobOrderDet.getOrderStatus()==(byte)3) {
                    if (StringUtils.isEmpty(id)) {
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    total += wmsInnerJobOrderDet.getDistributionQty().doubleValue();
                    num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(WmsInnerJobOrderDet.builder()
                            .jobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId())
                            .orderStatus((byte) 5)
                            .actualQty(wmsInnerJobOrderDet.getDistributionQty())
                            .modifiedUserId(sysUser.getUserId())
                            .modifiedTime(new Date())
                                    .workStartTime(new Date())
                            .build());

                    //???????????????????????????
                    searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

                    //????????????????????????
                    num+= this.Inventory(oldDto,wmsInnerJobOrderDetDto);

                    wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getDistributionQty());
                    //???????????????????????????
                    num += this.writeDeliveryOrderQty(wmsInnerJobOrderDet);

                    //??????redis
                    this.removeRedis(wmsInnerJobOrderDet.getJobOrderDetId());
                }
            }
            BigDecimal resultQty = wmsInnerJobOrderDets.stream()
                    .map(WmsInnerJobOrderDet::getDistributionQty)
                    .reduce(BigDecimal.ZERO,BigDecimal::add);
                //?????????????????????????????????
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                        .orderStatus((byte) 5)
                        .jobOrderId(wmsInnerJobOrder.getJobOrderId())
                        .actualQty(resultQty)
                        .workStartTime(new Date())
                        .workEndtTime(new Date())
                        .build());

            }
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets) {
        SysUser sysUser = currentUser();
        int num = 0;
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInPutawayOrderDets.get(0).getJobOrderId());


        //???????????????????????????
        if(wmsInnerJobOrder.getOrderTypeId()==8){
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
            wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInPutawayOrderDets);
            return this.PickingConfirmation(wmsInnerJobOrder,0);
        }


        for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInPutawayOrderDets) {
            if(wmsInnerJobOrder.getOrderStatus()==(byte)5){
                throw new BizErrorException("?????????????????????");
            }

            SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
            searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            WmsInnerJobOrderDetDto oldDto = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet).get(0);

            BigDecimal aqty = wmsInPutawayOrderDet.getActualQty();
            Long jobOrderDetId = null;
            if(wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getDistributionQty())==-1){
                WmsInnerJobOrderDet wms = new WmsInnerJobOrderDet();
                BeanUtil.copyProperties(wmsInPutawayOrderDet,wms);
                wms.setJobOrderDetId(null);
                wms.setPlanQty(wmsInPutawayOrderDet.getActualQty());
                wms.setDistributionQty(wmsInPutawayOrderDet.getActualQty());
                wms.setOrderStatus((byte)5);
                wms.setWorkStartTime(new Date());
                num+=wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wms);
                jobOrderDetId = wms.getJobOrderDetId();

                wmsInPutawayOrderDet.setOrderStatus((byte)3);
                wmsInPutawayOrderDet.setInStorageId(null);
                wmsInPutawayOrderDet.setPlanQty(wmsInPutawayOrderDet.getPlanQty().subtract(wms.getPlanQty()));
                wmsInPutawayOrderDet.setDistributionQty(wmsInPutawayOrderDet.getDistributionQty().subtract(wms.getDistributionQty()));
                wmsInPutawayOrderDet.setActualQty(null);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
            }else if(wmsInPutawayOrderDet.getDistributionQty().compareTo(wmsInPutawayOrderDet.getPlanQty())==0){
                //????????????
                wmsInPutawayOrderDet.setOrderStatus((byte)5);
                wmsInPutawayOrderDet.setModifiedUserId(sysUser.getUserId());
                wmsInPutawayOrderDet.setModifiedTime(new Date());
                wmsInPutawayOrderDet.setWorkStartTime(new Date());
                num += wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                jobOrderDetId = wmsInPutawayOrderDet.getJobOrderDetId();
            }
            //????????????
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setJobOrderId(wmsInPutawayOrderDet.getJobOrderId());
            WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
            WmsInnerJobOrderDet Det = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);

            //????????????????????????
            searchWmsInnerJobOrderDet.setJobOrderDetId(jobOrderDetId);
            List<WmsInnerJobOrderDetDto> wmsInner = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
            num+= this.Inventory(oldDto,wmsInner.get(0));

            //???????????????????????????
            num+=this.writeDeliveryOrderQty(Det);


            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
            int count = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            int oCount = wmsInnerJobOrderDetMapper.selectCount(wmsInnerJobOrderDet);


            if(oCount==count){
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setOrderStatus((byte)5);
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(ws.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                ws.setWorkEndtTime(new Date());
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
                //??????redis
                this.removeRedis(wmsInPutawayOrderDet.getJobOrderDetId());
            }else{
                WmsInnerJobOrder ws = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDto.getJobOrderId());
                ws.setJobOrderId(wmsInnerJobOrderDto.getJobOrderId());
                if(StringUtils.isEmpty(ws.getActualQty())){
                    ws.setActualQty(new BigDecimal("0.00"));
                }
                ws.setActualQty(ws.getActualQty().add(aqty));
                ws.setOrderStatus((byte)4);
                ws.setModifiedUserId(sysUser.getUserId());
                ws.setModifiedTime(new Date());
                if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                    ws.setWorkStartTime(new Date());
                }
                num +=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(ws);
            }
        }
        return num;
    }

    @Override
    public List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        SysUser sysUser = currentUser();
        searchWmsInnerJobOrder.setOrgId(sysUser.getOrganizationId());
        return wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
    }
    /**
     * ??????????????????
     * @param wmsInnerJobOrder
     * @param wmsInnerJobOrderDet
     */
    private int  DistributionInventory(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInnerJobOrderDet,int type,WmsInnerInventory wmsInnerInventory){
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        //????????????????????????
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetMapper.findList(searchWmsInnerJobOrderDet);
        if(list.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = list.get(0);
        wmsInnerJobOrderDetDto.setDistributionQty(wmsInnerJobOrderDet.getDistributionQty());
        wmsInnerJobOrderDetDto.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
        wmsInnerJobOrderDetDto.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
        wmsInnerJobOrderDetDto.setWarehouseName(wmsInnerJobOrderDto.getWarehouseName());
        int num = 0;
        num+=subtract(wmsInnerJobOrder,wmsInnerJobOrderDetDto,type,wmsInnerInventory);
        if(num>0){
            plus(wmsInnerJobOrder,wmsInnerJobOrderDetDto);
        }else {
            throw new BizErrorException("??????????????????");
        }
        return num;
    }

    /**
     * ?????????
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int subtract(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto,int type,WmsInnerInventory wmsInnerInventorys){
        List<WmsInnerInventory> wmsInnerInventory = new ArrayList<>();
        if(type==1 || type==3){
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
            if(!StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
            }
            criteria.andEqualTo("jobStatus",(byte)1);
            criteria.andEqualTo("storageId",wmsInnerJobOrderDetDto.getOutStorageId()).andEqualTo("warehouseId",wmsInnerJobOrderDetDto.getWarehouseId()).andGreaterThan("packingQty",0).andEqualTo("orgId",wmsInnerJobOrderDetDto.getOrgId());
            criteria.andEqualTo("stockLock",0).andEqualTo("qcLock",0).andEqualTo("lockStatus",0);
            if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getInventoryStatusId())){
                criteria.andEqualTo("inventoryStatusId",wmsInnerJobOrderDetDto.getInventoryStatusId());
            }
            wmsInnerInventory = wmsInnerInventoryMapper.selectByExample(example);
        }else if(type==2){
            wmsInnerInventory.add(wmsInnerInventorys);
        }
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException("??????????????????");
        }
        BigDecimal acuQty = wmsInnerJobOrderDetDto.getDistributionQty();
        //????????????
        BigDecimal countQty = wmsInnerInventory.stream()
                .map(li->{
                    if(StringUtils.isEmpty(li.getPackingQty())){
                        return BigDecimal.ZERO;
                    }else {
                        return li.getPackingQty();
                    }
                })
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        if(acuQty.compareTo(countQty)==1){
            throw new BizErrorException("????????????");
        }
        int num = 0;
        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
        if(redisUtil.hasKey(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString())){
            bigDecimalMap = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString());
        }
        for (WmsInnerInventory innerInventory : wmsInnerInventory) {
            if(acuQty.compareTo(BigDecimal.ZERO)==1) {
                if (innerInventory.getPackingQty().compareTo(acuQty) == -1 && innerInventory.getPackingQty().compareTo(BigDecimal.ZERO) == 1) {
                    if (bigDecimalMap.containsKey(innerInventory.getInventoryId())) {
                        bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(innerInventory.getPackingQty()));
                    } else {
                        bigDecimalMap.put(innerInventory.getInventoryId(), innerInventory.getPackingQty());
                    }
                    acuQty = acuQty.subtract(innerInventory.getPackingQty());
                    if (type == 3) {
                        //??????????????????
                        InventoryLogUtil.addLog(innerInventory, wmsInnerJobOrder, wmsInnerJobOrderDetDto, innerInventory.getPackingQty(), innerInventory.getPackingQty(), (byte) 4, (byte) 2);
                    }
                    innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(innerInventory.getPackingQty()));
                } else if (innerInventory.getPackingQty().compareTo(acuQty) > -1) {
                    if (type == 3) {
                        //??????????????????
                        InventoryLogUtil.addLog(innerInventory, wmsInnerJobOrder, wmsInnerJobOrderDetDto, innerInventory.getPackingQty(), acuQty, (byte) 4, (byte) 2);
                    }
                    innerInventory.setPackingQty(innerInventory.getPackingQty().subtract(acuQty));
                    if (bigDecimalMap.containsKey(innerInventory.getInventoryId())) {
                        bigDecimalMap.put(innerInventory.getInventoryId(), bigDecimalMap.get(innerInventory.getInventoryId()).add(acuQty));
                    } else {
                        bigDecimalMap.put(innerInventory.getInventoryId(), acuQty);
                    }
                    acuQty = acuQty.subtract(acuQty);
                }
                num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(innerInventory);
            }
        }
        redisUtil.set(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString(),bigDecimalMap);
        return num;
    }

    /**
     * ?????????????????????
     * @return
     */
    private BigDecimal getInvQty(Long jobOrderDetId,BigDecimal chageQty){
        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId.toString())){
            bigDecimalMap = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
        }
        for (Map.Entry<Long, BigDecimal> m : bigDecimalMap.entrySet()){
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException("??????????????????");
            }
            chageQty = chageQty.add(wmsInnerInventory.getPackingQty());
        }
        return chageQty;
    }

    /**
     * ????????????
     * @param wmsInnerJobOrderDetDto
     * @return
     */
    private int plus(WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto){
        SysUser sysUser = currentUser();

        Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+wmsInnerJobOrderDetDto.getJobOrderDetId().toString());
        WmsInnerInventory wmsInnerInventory = null;
        for (Map.Entry<Long, BigDecimal> m : map.entrySet()) {
            wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
            if(wmsInnerJobOrder!=null){
                break;
            }
        }

        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        int num = 0;
        SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
        searchWmsInnerJobOrder.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
        WmsInnerJobOrderDto wmsInnerJobOrderDto = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder).get(0);
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInnerJobOrderDetDto.getMaterialId());
//        if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getDistributionQty())){
//            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
//        }

        //?????????????????????????????? 2021-09-14 huangshuijun
        if(StringUtils.isNotEmpty(wmsInnerJobOrderDetDto.getBatchCode())){
            criteria.andEqualTo("batchCode",wmsInnerJobOrderDetDto.getBatchCode());
        }
        criteria.andEqualTo("jobStatus",(byte)2);
        criteria.andEqualTo("jobOrderDetId",wmsInnerJobOrderDetDto.getJobOrderDetId());
        criteria.andEqualTo("storageId",wmsInnerJobOrderDetDto.getOutStorageId()).andEqualTo("warehouseId",wmsInnerJobOrderDetDto.getWarehouseId());
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        BigDecimal qty = BigDecimal.ZERO;
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //????????????????????????
            WmsInnerInventory wms = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,wms);
            wms.setInventoryId(null);
            wms.setMaterialOwnerId(wmsInnerJobOrderDto.getMaterialOwnerId());
            wms.setWarehouseId(wmsInnerJobOrderDto.getWarehouseId());
            wms.setStorageId(wmsInnerJobOrderDetDto.getOutStorageId());
            wms.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wms.setPackingQty(wmsInnerJobOrderDetDto.getDistributionQty());
            wms.setPackingUnitName(wmsInnerJobOrderDetDto.getPackingUnitName());
            wms.setMaterialId(wmsInnerJobOrderDetDto.getMaterialId());
            wms.setJobStatus((byte)2);
            wms.setJobOrderDetId(wmsInnerJobOrderDetDto.getJobOrderDetId());
            wms.setCreateTime(new Date());
            wms.setCreateUserId(sysUser.getUserId());
            wms.setModifiedUserId(sysUser.getUserId());
            wms.setModifiedTime(new Date());
            wms.setOrgId(sysUser.getOrganizationId());
            num = wmsInnerInventoryMapper.insertSelective(wms);
            if(num<1){
                throw new BizErrorException("??????????????????");
            }
        }else{
            qty = wmsInnerInventorys.getPackingQty();
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInnerJobOrderDetDto.getDistributionQty()));
            wmsInnerInventorys.setModifiedTime(new Date());
            wmsInnerInventorys.setModifiedUserId(sysUser.getUserId());
            num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
            if(num<1){
                throw new BizErrorException("??????????????????");
            }
        }
        //??????????????????
        //InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInnerJobOrderDetDto,qty,wmsInnerJobOrderDetDto.getDistributionQty(),(byte)4,(byte)1);
        return num;
    }

    /**
     * ??????
     * @return
     */
    private int Inventory(WmsInnerJobOrderDetDto oldDto,WmsInnerJobOrderDetDto newDto){
        SysUser sysUser = currentUser();
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(oldDto.getJobOrderId());
        //???
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",oldDto.getMaterialId()).andEqualTo("warehouseId",oldDto.getWarehouseId()).andEqualTo("storageId",oldDto.getOutStorageId());
        if(!StringUtils.isEmpty(oldDto.getBatchCode())){
            criteria.andEqualTo("batchCode",oldDto.getBatchCode());
        }
        criteria.andEqualTo("jobOrderDetId",oldDto.getJobOrderDetId());
        criteria.andEqualTo("jobStatus",(byte)2);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        //????????????
        InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,wmsInnerInventory.getPackingQty(),newDto.getActualQty(),(byte)4,(byte)2);

        WmsInnerInventory wmsIn = new WmsInnerInventory();
        wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
        wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(newDto.getActualQty()));
        int num = wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);


        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",newDto.getMaterialId()).andEqualTo("warehouseId",newDto.getWarehouseId()).andEqualTo("storageId",newDto.getInStorageId());
        if(!StringUtils.isEmpty(newDto.getBatchCode())){
            criteria1.andEqualTo("batchCode",newDto.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",newDto.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        criteria1.andEqualTo("orgId",sysUser.getOrganizationId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //????????????
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(newDto.getInStorageId());
            inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(newDto.getActualQty());
            inv.setJobStatus((byte)2);
            inv.setJobOrderDetId(newDto.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateTime(new Date());
            inv.setCreateUserId(sysUser.getUserId());
            inv.setModifiedUserId(sysUser.getUserId());
            inv.setModifiedTime(new Date());
            inv.setOrgId(sysUser.getOrganizationId());

            //????????????
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,newDto,BigDecimal.ZERO,inv.getPackingQty(),(byte)4,(byte)1);
            return wmsInnerInventoryMapper.insertSelective(inv);
        }else{
            //?????????

            //????????????
            InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,oldDto,wmsInnerInventorys.getPackingQty(),newDto.getActualQty(),(byte)4,(byte)1);
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(newDto.getActualQty()));
            return wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    /**
     * ????????????
     * @return
     */
    private int  reconver(Long jobOrderDetId){
        //??????redis
        int num = 0;

        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId.toString())){
            Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
            for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(m.getKey());
                if(StringUtils.isEmpty(wmsInnerInventory)){
                    throw new BizErrorException("??????????????????");
                }
                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().add(new BigDecimal(String.valueOf(m.getValue()))));
                num+=wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);
            }
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }else{
            throw new BizErrorException("????????????????????????");
        }
        return num;
    }

    /**
     * ??????redis
     */
    private void removeRedis(Long jobOrderDetId){
        if(redisUtil.hasKey(this.REDIS_KEY+jobOrderDetId)){
            Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get(this.REDIS_KEY+jobOrderDetId.toString());
            //??????3????????????
            redisUtil.expire(this.REDIS_KEY+jobOrderDetId,3);
        }
    }

    /**
     * ?????????????????????????????????
     * @param wmsInnerJobOrderDet
     * @return
     */
    private int writeDeliveryOrderQty(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet = new WmsOutDeliveryOrderDet();
        wmsOutDeliveryOrderDet.setDeliveryOrderDetId(wmsInnerJobOrderDet.getSourceDetId());
        wmsOutDeliveryOrderDet.setPickingQty(wmsInnerJobOrderDet.getActualQty());
        ResponseEntity responseEntity = outFeignApi.update(wmsOutDeliveryOrderDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }

    /**
     * ?????????
     * @param wmsInnerJobOrderDet
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int retrographyStatus(WmsInnerJobOrderDet wmsInnerJobOrderDet){
        wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);
        Example example = new Example(WmsInnerJobOrderDet.class);
        example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrderDet.getJobOrderId());
        List<WmsInnerJobOrderDet> list = wmsInnerJobOrderDetMapper.selectByExample(example);
        byte by = 4;
        if(list.size()==list.stream().filter(li->li.getOrderStatus()==6).collect(Collectors.toList()).size()){
            by=6;
        }
        if(list.stream().filter(li->li.getOrderStatus()==5).collect(Collectors.toList()).size()==list.size()){
            by=5;
        }
        return wmsInnerJobOrderMapper.updateByPrimaryKeySelective(WmsInnerJobOrder.builder()
                .jobOrderId(wmsInnerJobOrderDet.getJobOrderId())
                .orderStatus(by)
                        .workEndtTime(by==6?new Date():null)
                .build());
    }


    /**
     * ????????????
     */


    /**
     * ????????????
     * @param outDeliveryOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int autoOutOrder(Long outDeliveryOrderId,Byte orderTypeId) {
        SysUser sysUser = currentUser();
        //????????????????????????????????????????????????
        Example example = new Example(WmsInnerJobOrder.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("sourceOrderId",outDeliveryOrderId).andEqualTo("jobOrderType",4);
        //???????????????
        if(orderTypeId==8){
            List<Byte> bytes = new ArrayList<>();
            bytes.add((byte)4);
            bytes.add((byte)5);
            criteria.andIn("orderStatus",bytes);
        }else {
            criteria.andEqualTo("orderStatus",5);
        }
        List<WmsInnerJobOrder> list = wmsInnerJobOrderMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("??????????????????????????????");
        }

        if(list.get(0).getOrderTypeId()==8){
            //?????????????????????
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("stockRequisition");
            List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(itemList.size()<1){
                throw new BizErrorException("?????????????????????????????????");
            }
            Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
            if(map.get("lack").equals("false") && map.get("beyond").equals("false")){
                if(list.size()>list.stream().filter(li->li.getOrderStatus()==(byte)5).collect(Collectors.toList()).size()){
                    throw new BizErrorException("???????????????,????????????");
                }
            }else {
                //????????????????????????????????????
                if(list.size()>list.stream().filter(li->li.getOrderStatus()==(orderTypeId==8?(byte)4:(byte)5)).collect(Collectors.toList()).size()){
                    throw new BizErrorException("???????????????,????????????");
                }
            }
        }else {
            //????????????????????????????????????
            if(list.size()>list.stream().filter(li->li.getOrderStatus()==(byte)5).collect(Collectors.toList()).size()){
                throw new BizErrorException("???????????????,????????????");
            }
        }
        //???????????????
        WmsOutDespatchOrder wmsOutDespatchOrder = new WmsOutDespatchOrder();
        List<WmsOutDespatchOrderReJo> wmsOutDespatchOrderReJos = new ArrayList<>();
        List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>();
        for (WmsInnerJobOrder wmsInnerJobOrder : list) {
            //???????????????????????????
            if(wmsInnerJobOrder.getOrderTypeId()==8){
                List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new ArrayList<>();
                example = new Example(WmsInnerJobOrderDet.class);
                example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
                List<WmsInnerJobOrderDet> dets = wmsInnerJobOrderDetMapper.selectByExample(example);

                //??????????????????????????????
                if(dets.stream().filter(li->li.getOrderStatus()==4 || li.getOrderStatus()==5).collect(Collectors.toList()).size()<1){
                    throw new BizErrorException("?????????????????????");
                }

                if(dets.stream().filter(li->StringUtils.isNotEmpty(li.getActualQty()) && li.getActualQty().compareTo(li.getDistributionQty())==-1).collect(Collectors.toList()).size()>0){
                    //???????????????
                    //?????????????????????
                    SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                    searchSysSpecItem.setSpecCode("stockRequisition");
                    List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                    Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
                    if(map.get("lack").equals("false")){
                        throw new BizErrorException("??????????????????????????????");
                    }else {
                        for (WmsInnerJobOrderDet det : dets) {
                            if(StringUtils.isNotEmpty(det.getActualQty()) && det.getActualQty().compareTo(det.getDistributionQty())==-1){
                                wmsInnerJobOrderDets.add(det);
                            }
                        }
                    }
                }
                if(wmsInnerJobOrderDets.size()>0){
                    wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
                    wmsInnerJobOrders.add(wmsInnerJobOrder);
                }
            }
            wmsOutDespatchOrder.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            WmsOutDespatchOrderReJo wms = new WmsOutDespatchOrderReJo();
            wms.setJobOrderId(wmsInnerJobOrder.getJobOrderId());
            wmsOutDespatchOrderReJos.add(wms);
        }

        if(wmsInnerJobOrders.size()>0){
            this.pickDisQty(wmsInnerJobOrders);
        }

        wmsOutDespatchOrder.setActualDespatchTime(new Date());
        wmsOutDespatchOrder.setPlanDespatchTime(new Date());
        wmsOutDespatchOrder.setWmsOutDespatchOrderReJo(wmsOutDespatchOrderReJos);
        ResponseEntity<String> responseEntity = outFeignApi.add(wmsOutDespatchOrder);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getMessage());
        }

        //??????
        ResponseEntity rs = outFeignApi.forwarding(responseEntity.getData());
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int sealOrder(List<Long> outDeliveryOrderIds,Byte type) {
        int num=0;
        if(StringUtils.isEmpty(type)){
            for (Long outDeliveryOrderId : outDeliveryOrderIds) {
                //???????????????????????????????????????
                Example example = new Example(WmsInnerJobOrder.class);
                example.createCriteria().andEqualTo("sourceOrderId",outDeliveryOrderId).andEqualTo("orderTypeId",8).andNotEqualTo("orderStatus",6);
                List<WmsInnerJobOrder> wmsInnerJobOrders = wmsInnerJobOrderMapper.selectByExample(example);
                num = this.sealOrderDet(wmsInnerJobOrders);

                if(wmsInnerJobOrders.size()>0 && wmsInnerJobOrders.get(0).getOrderTypeId().equals(8L)) {
                    SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
                    searchSysSpecItemFiveRing.setSpecCode("FiveRing");
                    List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
                    if (itemListFiveRing.size() < 1) {
                        throw new BizErrorException("????????? FiveRing ????????????");
                    }
                    SysSpecItem sysSpecItem = itemListFiveRing.get(0);
                    if ("1".equals(sysSpecItem.getParaValue())) {
                        outFeignApi.overIssue(outDeliveryOrderId);
                    }
                }
            }
        }else if(type==1){
            //????????????21???????????????
            SearchWmsInnerJobOrder searchWmsInnerJobOrder = new SearchWmsInnerJobOrder();
            searchWmsInnerJobOrder.setSealOrder((byte)1);
            List<WmsInnerJobOrderDto> list = wmsInnerJobOrderMapper.findList(searchWmsInnerJobOrder);
            if(list.size()>0){
                List<WmsInnerJobOrder> wmsInnerJobOrders = new ArrayList<>(list);
                num = this.sealOrderDet(wmsInnerJobOrders);
            }
        }
        return  num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BigDecimal chechkBarcodeToWanbao(String barCode, Long jobOrderDetId) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderDetMapper.selectByPrimaryKey(jobOrderDetId);
        if(StringUtils.isEmpty(wmsInnerJobOrderDet)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        if(wmsInnerJobOrderDet.getOrderStatus()==5){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"????????????????????????????????????");
        }
        WmsInnerJobOrder wmsInnerJobOrder = wmsInnerJobOrderMapper.selectByPrimaryKey(wmsInnerJobOrderDet.getJobOrderId());
        if(StringUtils.isEmpty(wmsInnerJobOrder)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404);
        }
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",wmsInnerJobOrderDet.getMaterialId())
                .andEqualTo("storageId",wmsInnerJobOrderDet.getOutStorageId())
                //.andEqualTo("barcodeStatus",3)
                .andEqualTo("orgId",sysUser.getOrganizationId());
        //weekend?????? ?????????????????????????????????????????????
        Weekend<WmsInnerInventoryDet> weekend = new Weekend<>(WmsInnerInventoryDet.class);
        WeekendCriteria<WmsInnerInventoryDet,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.orEqualTo(WmsInnerInventoryDet::getBarcode,barCode).orEqualTo(WmsInnerInventoryDet::getSalesBarcode,barCode).orEqualTo(WmsInnerInventoryDet::getCustomerBarcode,barCode);
        weekend.and(criteria);
        List<WmsInnerInventoryDet> wmsInnerInventoryDets = wmsInnerInventoryDetMapper.selectByExample(weekend);
        if(wmsInnerInventoryDets.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"??????:"+barCode+"????????????!");
        }
        if(StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())){
            wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
        }
        BigDecimal qty = BigDecimal.ZERO;
        for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
            if(wmsInnerInventoryDet.getBarcodeStatus()==4){
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"?????????"+barCode+"????????????????????????");
            }else if(wmsInnerInventoryDet.getBarcodeStatus()!=3){
                throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"?????????"+barCode+"??????????????????????????????");
            }

            if(StringUtils.isNotEmpty(wmsInnerInventoryDet.getLockStatus()) && wmsInnerInventoryDet.getLockStatus()==(byte)1){
                throw new BizErrorException("???????????????????????? ???????????????????????????-->"+barCode);
            }

            wmsInnerInventoryDet.setStorageId(wmsInnerJobOrderDet.getInStorageId());
            wmsInnerInventoryDet.setDeliveryOrderCode(wmsInnerJobOrder.getJobOrderCode());
            wmsInnerInventoryDet.setDeliverDate(new Date());
            wmsInnerInventoryDet.setBarcodeStatus((byte)4);
            wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryDet);

            //??????????????????
            wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getActualQty().add(wmsInnerInventoryDet.getMaterialQty()));
            qty = qty.add(wmsInnerInventoryDet.getMaterialQty());
        }
        if(wmsInnerJobOrderDet.getActualQty().compareTo(wmsInnerJobOrderDet.getDistributionQty())==1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"????????????????????????????????????");
        }
        //?????????????????????????????????????????????????????????????????????
        if(wmsInnerJobOrderDet.getActualQty().compareTo(wmsInnerJobOrderDet.getDistributionQty())==0){
            wmsInnerJobOrderDet.setOrderStatus((byte)5);
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
        }else {
            if(StringUtils.isEmpty(wmsInnerJobOrderDet.getWorkStartTime())){
                wmsInnerJobOrderDet.setWorkStartTime(new Date());
            }
            wmsInnerJobOrderDet.setOrderStatus((byte)4);
        }
        wmsInnerJobOrderDet.setModifiedTime(new Date());
        wmsInnerJobOrderDet.setModifiedUserId(sysUser.getUserId());
        wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

        //????????????
        WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = new WmsInnerJobOrderDetDto();
        BeanUtil.copyProperties(wmsInnerJobOrderDet,wmsInnerJobOrderDetDto);
        wmsInnerJobOrderDetDto.setActualQty(qty);
        this.Inventory(wmsInnerJobOrderDetDto,wmsInnerJobOrderDetDto);

        //??????????????????
        if(wmsInnerJobOrderDet.getOrderStatus()==4){
            if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                wmsInnerJobOrder.setWorkStartTime(new Date());
            }
            wmsInnerJobOrder.setOrderStatus((byte)4);
        }else {
            example = new Example(WmsInnerJobOrderDet.class);
            example.createCriteria().andEqualTo("jobOrderId",wmsInnerJobOrder.getJobOrderId());
            List<WmsInnerJobOrderDet> detList = wmsInnerJobOrderDetMapper.selectByExample(example);
            if(detList.stream().filter(y->y.getOrderStatus()==5).collect(Collectors.toList()).size()==detList.size()){
                wmsInnerJobOrder.setOrderStatus((byte)5);
            }else {
                wmsInnerJobOrder.setOrderStatus((byte)4);
            }
        }
        wmsInnerJobOrder.setModifiedTime(new Date());
        wmsInnerJobOrder.setModifiedUserId(sysUser.getUserId());
        wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        //???????????????????????????
        this.writeDeliveryOrderQty(wmsInnerJobOrderDet);
//        List<String> list = wmsInnerInventoryDets.stream().map(WmsInnerInventoryDet::getBarcode).collect(Collectors.toList());
//        String barcodes = list.stream().map(String::valueOf).collect(Collectors.joining(","));
//        BigDecimal qty = wmsInnerInventoryDets.stream().map(e -> e.getMaterialQty()).reduce(BigDecimal::add).get();
//        Map<String,Object> map = new HashMap<>();
        return qty;
    }

    /**
     * ????????????
     * @param wmsInnerJobOrders
     * @return
     */
    private int sealOrderDet(List<WmsInnerJobOrder> wmsInnerJobOrders){
        int num = 0;
        for (WmsInnerJobOrder wmsInnerJobOrder : wmsInnerJobOrders) {
            Example example1 = new Example(WmsInnerJobOrderDet.class);
            example1.createCriteria().andEqualTo("jobOrderId", wmsInnerJobOrder.getJobOrderId()).andNotEqualTo("orderStatus", 6);
            List<WmsInnerJobOrderDet> detList = wmsInnerJobOrderDetMapper.selectByExample(example1);
            for (WmsInnerJobOrderDet wmsInnerJobOrderDet : detList) {
                Map<Byte, BigDecimal> map = new HashMap<>();
                if (StringUtils.isEmpty(wmsInnerJobOrderDet.getActualQty())) {
                    wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                }
                if (wmsInnerJobOrderDet.getOrderStatus() >= 3) {
                    BigDecimal sealQty = wmsInnerJobOrderDet.getDistributionQty().subtract(wmsInnerJobOrderDet.getActualQty());
                    map.put((byte) 1, sealQty);
                    if (wmsInnerJobOrderDet.getActualQty().compareTo(BigDecimal.ZERO) == 1) {
                        map.put((byte) 2, wmsInnerJobOrderDet.getActualQty());
                    }
                } else {
                    if (StringUtils.isEmpty(wmsInnerJobOrderDet.getDistributionQty())) {
                        wmsInnerJobOrderDet.setDistributionQty(BigDecimal.ZERO);
                    }
                    BigDecimal sealQty = wmsInnerJobOrderDet.getPlanQty().subtract(wmsInnerJobOrderDet.getDistributionQty());
                    map.put((byte) 1, sealQty);
                    if (wmsInnerJobOrderDet.getActualQty().compareTo(BigDecimal.ZERO) == 1) {
                        map.put((byte) 2, wmsInnerJobOrderDet.getActualQty());
                    }
                }

                for (Map.Entry<Byte, BigDecimal> byteBigDecimalEntry : map.entrySet()) {
                    BigDecimal sealQty = byteBigDecimalEntry.getValue();
                    //????????????????????????0
                    if (sealQty.compareTo(BigDecimal.ZERO) == 1) {
                        //
                        Example example2 = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria = example2.createCriteria();
                        criteria.andEqualTo("relevanceOrderCode", wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId());

                        if(byteBigDecimalEntry.getKey()==1){
                            criteria.andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId());
                        }else {
                            criteria.andEqualTo("storageId", wmsInnerJobOrderDet.getInStorageId());
                        }
                        if (StringUtils.isNotEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                            criteria.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
                        } else {
                            criteria.andIsNull("batchCode");
                        }
                        criteria.andEqualTo("jobOrderDetId", wmsInnerJobOrderDet.getJobOrderDetId());
                        criteria.andEqualTo("jobStatus", (byte) 2);
                        criteria.andEqualTo("orgId", wmsInnerJobOrder.getOrgId());
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example2);
                        if (StringUtils.isEmpty(wmsInnerInventory)) {
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                        //????????????
                        //InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInnerJobOrderDet,wmsInnerInventory.getPackingQty(),sealQty,(byte)4,(byte)2);
                        num += wmsInnerInventoryMapper.deleteByPrimaryKey(wmsInnerInventory.getInventoryId());

                        //????????????
                        example2.clear();
                        Example.Criteria criterias = example2.createCriteria();
                        criterias.andEqualTo("materialId", wmsInnerJobOrderDet.getMaterialId()).andEqualTo("warehouseId", wmsInnerJobOrderDet.getWarehouseId()).andEqualTo("storageId", wmsInnerJobOrderDet.getOutStorageId());
                        if (StringUtils.isNotEmpty(wmsInnerJobOrderDet.getBatchCode())) {
                            criterias.andEqualTo("batchCode", wmsInnerJobOrderDet.getBatchCode());
                        } else {
                            criterias.andIsNull("batchCode");
                        }
                        criterias.andEqualTo("jobStatus", (byte) 1);
                        criterias.andEqualTo("orgId", wmsInnerJobOrder.getOrgId());
                        criteria.andGreaterThan("packingQty",0);
                        criteria.andEqualTo("inventoryStatusId",wmsInnerJobOrderDet.getInventoryStatusId());
                        WmsInnerInventory innerInventory = wmsInnerInventoryMapper.selectOneByExample(example2);
                        if (StringUtils.isEmpty(innerInventory)) {
                            BeanUtil.copyProperties(wmsInnerInventory, innerInventory);
                            innerInventory.setInventoryId(null);
                            innerInventory.setJobOrderDetId(null);
                            innerInventory.setJobStatus((byte) 1);
                            num += wmsInnerInventoryMapper.insertSelective(innerInventory);
                        } else {
                            if (StringUtils.isEmpty(innerInventory.getPackingQty())) {
                                innerInventory.setPackingQty(BigDecimal.ZERO);
                            }
                            //????????????
                            Long outStorageId = wmsInnerJobOrderDet.getOutStorageId();
                            wmsInnerJobOrderDet.setOutStorageId(wmsInnerJobOrderDet.getInStorageId());
                            InventoryLogUtil.addLog(wmsInnerInventory, wmsInnerJobOrder, wmsInnerJobOrderDet, wmsInnerInventory.getPackingQty(), sealQty, (byte) 4, (byte) 1);
                            wmsInnerJobOrderDet.setOutStorageId(outStorageId);
                            innerInventory.setPackingQty(innerInventory.getPackingQty().add(sealQty));
                            num += wmsInnerInventoryMapper.updateByPrimaryKeySelective(innerInventory);
                        }
                    }
                    if(byteBigDecimalEntry.getKey()==2){
                        wmsInnerJobOrder.setActualQty(wmsInnerJobOrder.getActualQty().subtract(sealQty));
                    }
                    wmsInnerJobOrderDet.setActualQty(BigDecimal.ZERO);
                    wmsInnerJobOrderDet.setDistributionQty(BigDecimal.ZERO);
                    wmsInnerJobOrderDet.setOrderStatus((byte) 6);
                    if (StringUtils.isEmpty(wmsInnerJobOrderDet.getWorkStartTime())) {
                        wmsInnerJobOrderDet.setWorkStartTime(new Date());
                    }
                    wmsInnerJobOrderDet.setWorkEndTime(new Date());
                    wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInnerJobOrderDet);

                    //??????redis
                    this.removeRedis(wmsInnerJobOrderDet.getJobOrderDetId());
                }
                wmsInnerJobOrder.setOrderStatus((byte) 6);
                if (StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())) {
                    wmsInnerJobOrder.setWorkStartTime(new Date());
                }
                wmsInnerJobOrder.setWorkEndtTime(new Date());
                wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

                //???????????????
                ResponseEntity responseEntity = outFeignApi.sealOrder(wmsInnerJobOrder.getSourceOrderId());
                if (responseEntity.getCode() != 0) {
                    throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
                }
            }
        }
        return num;
    }

    private int pickDisQty(List<WmsInnerJobOrder> wmsInnerJobOrders){
        int num = 0;
        for (WmsInnerJobOrder wmsInnerJobOrder : wmsInnerJobOrders) {
            for (WmsInnerJobOrderDet det : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                //???????????????
                //??????????????????????????????
                if(det.getActualQty().compareTo(det.getDistributionQty())==-1){
                    //???????????????????????? = ????????????-????????????
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
                    wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
                    if(redisUtil.hasKey("PICKINGID:"+det.getJobOrderDetId())){
                        Example example = new Example(WmsInnerInventory.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",det.getMaterialId()).andEqualTo("warehouseId",det.getWarehouseId()).andEqualTo("storageId",det.getOutStorageId());
                        if(!StringUtils.isEmpty(wmsInnerJobOrderDet.getBatchCode())){
                            criteria.andEqualTo("batchCode",det.getBatchCode());
                        }
                        criteria.andEqualTo("jobOrderDetId",det.getJobOrderDetId());
                        criteria.andEqualTo("jobStatus",(byte)2);
                        criteria.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
                        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
                        if(StringUtils.isEmpty(wmsInnerInventory)){
                            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                        wmsInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
                        wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventory);

                        //??????????????? = ??????-??????
                        BigDecimal pickQty = det.getDistributionQty().subtract(det.getActualQty());
                        Map<Long,BigDecimal> map = (Map<Long, BigDecimal>) redisUtil.get("PICKINGID:"+det.getJobOrderDetId().toString());
                        Map<Long,BigDecimal> bigDecimalMap = new HashMap<>();
                        for (Map.Entry<Long, BigDecimal> m : map.entrySet()){
                            BigDecimal mQty = new BigDecimal(String.valueOf(m.getValue()));
                            if(pickQty.compareTo(BigDecimal.ZERO)==1){
                                if(mQty.compareTo(pickQty)==1){
                                    if(mQty.subtract(pickQty).compareTo(BigDecimal.ZERO)==1){
                                        bigDecimalMap.put(m.getKey(),mQty);
                                    }
                                    pickQty.subtract(mQty);
                                }
                            }else {
                                bigDecimalMap.put(m.getKey(),mQty);
                            }
                        }
                        //??????3????????????
                        redisUtil.expire("PICKINGID:"+det.getJobOrderDetId(),3);

                        redisUtil.set("PICKINGID:"+wmsInnerJobOrderDet.getJobOrderDetId(),bigDecimalMap);
                    }
                    det.setPlanQty(det.getPlanQty().subtract(qty));
                    det.setDistributionQty(det.getDistributionQty().subtract(qty));
                    det.setWorkEndTime(new Date());
                    wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(det);
                }
            }
        }
        return num;
    }


    /**
     * ?????????????????????
     * //???????????????????????????????????????????????????????????????????????????????????????????????? ???????????????????????????????????? ??????????????????
     * @param wmsInnerJobOrder
     * @param allOrder ?????????????????? 1-?????? 0-??????
     * @return
     */
    private int PickingConfirmation(WmsInnerJobOrder wmsInnerJobOrder,Integer allOrder){
        int num = 0;
        wmsInnerJobOrder.setOrderStatus((byte)4);
        BigDecimal totalQty = BigDecimal.ZERO;
        //?????????????????????
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("stockRequisition");
        List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(itemList.size()<1){
            throw new BizErrorException("?????????????????????????????????");
        }
        Map<String ,String> map = JSONArray.parseObject(itemList.get(0).getParaValue(), Map.class);
            for (WmsInnerJobOrderDet wmsInPutawayOrderDet : wmsInnerJobOrder.getWmsInPutawayOrderDets()) {
                if (allOrder == 1){
                    //????????????????????????????????????????????????????????????????????????????????????
                    if (StringUtils.isEmpty(wmsInPutawayOrderDet.getActualQty()) || wmsInPutawayOrderDet.getActualQty().compareTo(wmsInPutawayOrderDet.getPlanQty()) == -1) {
                        BigDecimal actualQty = BigDecimal.ZERO;
                        if (StringUtils.isEmpty(wmsInPutawayOrderDet.getActualQty())) {
                            actualQty = wmsInPutawayOrderDet.getDistributionQty();
                            wmsInPutawayOrderDet.setActualQty(actualQty);
                        } else {
                            //??????=????????????-???????????????
                            actualQty = wmsInPutawayOrderDet.getDistributionQty().subtract(wmsInPutawayOrderDet.getActualQty());
                            wmsInPutawayOrderDet.setActualQty(wmsInPutawayOrderDet.getActualQty().add(actualQty));
                        }
                        //????????????????????????
                        if(map.get("beyond").equals("false")){
                            wmsInPutawayOrderDet.setOrderStatus((byte)5);
                        }else {
                            wmsInPutawayOrderDet.setOrderStatus((byte)4);
                        }
                        this.addDistribute(false,wmsInnerJobOrder,wmsInPutawayOrderDet);
                        wmsInPutawayOrderDet.setWorkEndTime(new Date());
                        totalQty = totalQty.add(actualQty);
                    }
                }else if(allOrder==0){
                    //??????????????????????????????
                    WmsInnerJobOrderDet det = wmsInnerJobOrderDetMapper.selectByPrimaryKey(wmsInPutawayOrderDet.getJobOrderDetId());
                    if(StringUtils.isNotEmpty(det.getActualQty()) && det.getActualQty().compareTo(det.getDistributionQty())>-1){
                        throw new BizErrorException("??????????????????????????????");
                    }
                    WmsInnerInventory wmsInnerInventory = null;
                    Boolean isBeyond = false;
                    //?????????????????????
                    if(StringUtils.isEmpty(det.getActualQty())){
                        // ????????????????????????????????????
                        if(wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())<1){
                            isBeyond = false;
                            if(wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())==0){
                                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                            }
                        }
                        //??????????????????????????????
                        else if (wmsInPutawayOrderDet.getActualQty().compareTo(det.getPlanQty())==1){
                            //??????????????????
                            if(map.get("beyond").equals("false")){
                                //???????????????
                                throw new BizErrorException("?????????????????????????????????");
                            }else {
                                isBeyond=true;
                            }
                        }
                    }else {
                        //??????????????????????????????????????????????????????
                        if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())<1){
                            isBeyond = false;
                            if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==0){
                                wmsInPutawayOrderDet.setWorkEndTime(new Date());
                            }
                        }
                        //????????????????????????????????????????????????
                        else if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==1){
                            //??????????????????
                            if(map.get("beyond").equals("false")){
                                //???????????????
                                throw new BizErrorException("?????????????????????????????????");
                            }else {
                                isBeyond=true;
                            }
                        }
                    }
                    if(map.get("lack").equals("false") && map.get("beyond").equals("false")){
                        if(StringUtils.isEmpty(det.getActualQty())){
                            det.setActualQty(BigDecimal.ZERO);
                        }
                        if(det.getActualQty().add(wmsInPutawayOrderDet.getActualQty()).compareTo(det.getPlanQty())==0) {
                            wmsInPutawayOrderDet.setOrderStatus((byte) 5);
                            wmsInPutawayOrderDet.setWorkEndTime(new Date());
                        }else {
                            wmsInPutawayOrderDet.setOrderStatus((byte)4);
                        }
                    }else {
                        wmsInPutawayOrderDet.setOrderStatus((byte)4);
                    }
                    totalQty = totalQty.add(wmsInPutawayOrderDet.getActualQty());
                    this.addDistribute(isBeyond,wmsInnerJobOrder,wmsInPutawayOrderDet);
                    if(StringUtils.isNotEmpty(det.getActualQty())){
                        wmsInPutawayOrderDet.setActualQty(wmsInPutawayOrderDet.getActualQty().add(det.getActualQty()));
                    }
                }
                if(totalQty.compareTo(BigDecimal.ZERO)==1){
                    //?????????
                    if(StringUtils.isEmpty(wmsInPutawayOrderDet.getWorkStartTime())){
                        wmsInPutawayOrderDet.setWorkStartTime(new Date());
                    }
                    //wmsInPutawayOrderDet.setOrderStatus((byte)4);
                    num+=wmsInnerJobOrderDetMapper.updateByPrimaryKeySelective(wmsInPutawayOrderDet);
                    //???????????????????????????
                    num+=this.writeDeliveryOrderQty(wmsInPutawayOrderDet);
                    if(wmsInPutawayOrderDet.getOrderStatus()==5){
                        //??????redis
                        this.removeRedis(wmsInPutawayOrderDet.getJobOrderDetId());
                    }
                }
            }
            if(StringUtils.isEmpty(wmsInnerJobOrder.getWorkStartTime())){
                wmsInnerJobOrder.setWorkStartTime(new Date());
            }
        //????????????????????????
        if(StringUtils.isEmpty(wmsInnerJobOrder.getActualQty())){
            wmsInnerJobOrder.setActualQty(totalQty);
        }else {
           wmsInnerJobOrder.setActualQty(wmsInnerJobOrder.getActualQty().add(totalQty));
        }
        if(wmsInnerJobOrder.getActualQty().compareTo(wmsInnerJobOrder.getPlanQty())>-1){
            wmsInnerJobOrder.setWorkEndtTime(new Date());
            if(map.get("beyond").equals("false")){
                wmsInnerJobOrder.setOrderStatus((byte)5);
            }else {
                wmsInnerJobOrder.setOrderStatus((byte)4);
            }
        }
        num+=wmsInnerJobOrderMapper.updateByPrimaryKeySelective(wmsInnerJobOrder);

        return num;
    }

    private void addDistribute(Boolean isBeyond,WmsInnerJobOrder wmsInnerJobOrder,WmsInnerJobOrderDet wmsInPutawayOrderDet){
        WmsInnerInventory wmsInnerInventory = null;
//        if(isBeyond){
//
//        }else {
            //??????????????????
            Example example = new Example(WmsInnerInventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("warehouseId",wmsInPutawayOrderDet.getWarehouseId()).andEqualTo("storageId",wmsInPutawayOrderDet.getOutStorageId());
            if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getBatchCode())){
                criteria.andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
            }
            criteria.andEqualTo("jobOrderDetId",wmsInPutawayOrderDet.getJobOrderDetId());
            criteria.andEqualTo("jobStatus",(byte)2);
            criteria.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
            wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
            if(StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            WmsInnerInventory wmsIn = new WmsInnerInventory();
            wmsIn.setInventoryId(wmsInnerInventory.getInventoryId());
            wmsIn.setPackingQty(wmsInnerInventory.getPackingQty());
            if(isBeyond){
                BigDecimal qty = wmsInPutawayOrderDet.getActualQty();
                if(StringUtils.isNotEmpty(wmsIn.getPackingQty()) && wmsInPutawayOrderDet.getActualQty().compareTo(wmsIn.getPackingQty())>-1){
                    InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInPutawayOrderDet,wmsInnerInventory.getPackingQty(),wmsIn.getPackingQty(),(byte)4,(byte)2);

                    qty = wmsInPutawayOrderDet.getActualQty().subtract(wmsIn.getPackingQty());
                    wmsIn.setPackingQty(wmsIn.getPackingQty().subtract(wmsIn.getPackingQty()));
                    wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
                }
                if(StringUtils.isNotEmpty(qty) && qty.compareTo(BigDecimal.ZERO)==1){
                    //????????????????????????
                    WmsInnerJobOrderDetDto wmsInnerJobOrderDetDto = new WmsInnerJobOrderDetDto();
                    BeanUtil.copyProperties(wmsInPutawayOrderDet,wmsInnerJobOrderDetDto);
                    wmsInnerJobOrderDetDto.setActualQty(qty);
                    this.subtract(wmsInnerJobOrder,wmsInnerJobOrderDetDto,3,null);
                }
            }else {
                InventoryLogUtil.addLog(wmsInnerInventory,wmsInnerJobOrder,wmsInPutawayOrderDet,getInvQty(wmsInPutawayOrderDet.getJobOrderDetId(),wmsInPutawayOrderDet.getActualQty()),wmsInPutawayOrderDet.getActualQty(),(byte)4,(byte)2);
                wmsIn.setPackingQty(wmsInnerInventory.getPackingQty().subtract(wmsInPutawayOrderDet.getActualQty()));
                wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsIn);
            }
        //}
        //?????????
        example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("relevanceOrderCode",wmsInnerJobOrder.getJobOrderCode()).andEqualTo("materialId",wmsInPutawayOrderDet.getMaterialId()).andEqualTo("warehouseId",wmsInPutawayOrderDet.getWarehouseId()).andEqualTo("storageId",wmsInPutawayOrderDet.getInStorageId());
        if(!StringUtils.isEmpty(wmsInPutawayOrderDet.getBatchCode())){
            criteria1.andEqualTo("batchCode",wmsInPutawayOrderDet.getBatchCode());
        }
        criteria1.andEqualTo("jobOrderDetId",wmsInPutawayOrderDet.getJobOrderDetId());
        criteria1.andEqualTo("jobStatus",(byte)2);
        criteria1.andEqualTo("orgId",wmsInnerJobOrder.getOrgId());
        WmsInnerInventory wmsInnerInventorys = wmsInnerInventoryMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(wmsInnerInventorys)){
            //????????????
            WmsInnerInventory inv = new WmsInnerInventory();
            BeanUtil.copyProperties(wmsInnerInventory,inv);
            inv.setStorageId(wmsInPutawayOrderDet.getInStorageId());
            inv.setWarehouseId(wmsInnerJobOrder.getWarehouseId());
            inv.setRelevanceOrderCode(wmsInnerJobOrder.getJobOrderCode());
            inv.setPackingQty(wmsInPutawayOrderDet.getActualQty());
            inv.setJobStatus((byte)2);
            inv.setJobOrderDetId(wmsInPutawayOrderDet.getJobOrderDetId());
            inv.setInventoryId(null);
            inv.setCreateTime(new Date());
            inv.setCreateUserId(wmsInnerJobOrder.getModifiedUserId());
            inv.setModifiedUserId(wmsInnerJobOrder.getModifiedUserId());
            inv.setModifiedTime(new Date());
            inv.setOrgId(wmsInnerJobOrder.getOrgId());
            wmsInnerInventoryMapper.insertSelective(inv);
            InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInPutawayOrderDet,BigDecimal.ZERO,inv.getPackingQty(),(byte)4,(byte)1);
        }else{
            //?????????
            InventoryLogUtil.addLog(wmsInnerInventorys,wmsInnerJobOrder,wmsInPutawayOrderDet,wmsInnerInventorys.getPackingQty(),wmsInPutawayOrderDet.getActualQty(),(byte)4,(byte)1);
            wmsInnerInventorys.setPackingQty(wmsInnerInventorys.getPackingQty().add(wmsInPutawayOrderDet.getActualQty()));
            wmsInnerInventoryMapper.updateByPrimaryKeySelective(wmsInnerInventorys);
        }
    }

    // ??????????????????????????????
    public String getFactoryBarcode(String barcode){
        String factoryBarcode = null;
        if (barcode.length() != 23){
            // ??????????????????????????????
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("wanbaoCheckBarcode");
            List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if (!specItems.isEmpty()){
                SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess = new SearchMesSfcBarcodeProcess();
                searchMesSfcBarcodeProcess.setIsCustomerBarcode(barcode);
                List<MesSfcBarcodeProcessDto> mesSfcBarcodeProcessDtos = sfcFeignApi.findList(searchMesSfcBarcodeProcess).getData();
                if (!mesSfcBarcodeProcessDtos.isEmpty()){
                    factoryBarcode = mesSfcBarcodeProcessDtos.get(0).getBarcode();
                }
            }
        }

        if (factoryBarcode == null) {
            SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance = new SearchMesSfcKeyPartRelevance();
            searchMesSfcKeyPartRelevance.setPartBarcode(barcode);
            List<MesSfcKeyPartRelevanceDto> mesSfcKeyPartRelevanceDtos = sfcFeignApi.findList(searchMesSfcKeyPartRelevance).getData();
            if (!mesSfcKeyPartRelevanceDtos.isEmpty()) {
                factoryBarcode = mesSfcKeyPartRelevanceDtos.get(0).getBarcodeCode();
            }else{
                factoryBarcode = barcode;
            }
        }
        return factoryBarcode;
    }

    /**
     * ????????????????????????
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
