package com.fantechs.provider.base.util;

import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.service.BaseMaterialService;
import com.fantechs.provider.base.service.BaseStorageCapacityService;
import com.fantechs.provider.base.service.BaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2021/6/24
 * 库位分配工具类
 */
@Component
public class StorageDistributionRuleUtils {

    //引用
    @Autowired
    private BaseStorageService baseStorageService;
    @Autowired
    private SecurityFeignApi securityFeignApi;
    @Autowired
    private BaseStorageCapacityService baseStorageCapacityService;
    @Autowired
    private BaseMaterialService baseMaterialService;

    //声明对象
    private static StorageDistributionRuleUtils storageDistributionRuleUtils;

    //初始化
    @PostConstruct
    public void init(){
        storageDistributionRuleUtils = this;
        storageDistributionRuleUtils.baseStorageService = this.baseStorageService;
        storageDistributionRuleUtils.securityFeignApi = this.securityFeignApi;
        storageDistributionRuleUtils.baseMaterialService = this.baseMaterialService;
        storageDistributionRuleUtils.baseStorageCapacityService = this.baseStorageCapacityService;
    }

    /**
     * 上架分配主规则
     * @param packageQty
     * @return
     */
    public static List<StorageRuleDto> JobMainRule(BigDecimal packageQty, Long warehouseId, Long materialId,
                                                   String batchCode, String proDate){
        //可用库位列表
        List<StorageRuleDto> list = new ArrayList<>();
        //待上架总数包装数量
        BigDecimal jobTotalPackageQty_BU = packageQty;
        //基准动线号排序：DOWN-降序、ASC-升序
        String SART = "ASC";
        //上架动线豪=号
        Integer putawayMoveLineNo =null;
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){
            //获取货品专用库位
            List<StorageRuleDto> CanPutawayStorageList = getCanPutawayStorageList(warehouseId,materialId);
            if(StringUtils.isNotEmpty(CanPutawayStorageList) && CanPutawayStorageList.size()>0){
                //专用库位分配子规则
                Map<String ,Object> map = dedicatedStorager(CanPutawayStorageList,jobTotalPackageQty_BU,list);
                list.addAll((List<StorageRuleDto>) map.get("list"));
                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
            }
        }
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || batchCode!=null){
            //获取批次相同库位
            if(StringUtils.isNotEmpty(batchCode)){
                List<StorageRuleDto>  BatchEqualStorageList = getBatchEqualStorageList(warehouseId,batchCode,proDate,materialId);
                if(StringUtils.isNotEmpty(BatchEqualStorageList) && BatchEqualStorageList.size()>0){
                    Map<String,Object> map = dedicatedStorager(BatchEqualStorageList,jobTotalPackageQty_BU,list);
                    list.addAll((List<StorageRuleDto>) map.get("list"));
                    jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
                }
            }
        }

        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || proDate!=null){
            //获取空库位
//            putawayMoveLineNo = storageDistributionRuleUtils.baseStorageService.findPutawayNo(warehouseId,materialId);
//            Map<String,Object> map = getCanPutawayEmptyStorageList(warehouseId,putawayMoveLineNo);
//            list = (List<StorageRuleDto>) map.get("list");
//            SART = map.get("SART")!=null?map.get("SART").toString():"ASC";
//            if(list.size()>0){
//                map = EmptyStorageRule(list,SART,jobTotalPackageQty_BU);
//                list = (List<StorageRuleDto>) map.get("list");
//                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
//            }
            //查询是否有最近上架库位
            List<StorageRuleDto> LasetStorage = LasetStorage(warehouseId,materialId);


            if(StringUtils.isNotEmpty(LasetStorage) && LasetStorage.size()>0){
                Map<String,Object> map = dedicatedStorager(LasetStorage,jobTotalPackageQty_BU,list);
                list.addAll((List<StorageRuleDto>) map.get("list"));
                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
            }
        }

        //获取空库位
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || proDate!=null){
            //获取空库位
            List<StorageRuleDto> EmptyStorageList = getCanPutawayEmptyStorageList(warehouseId,materialId);
            if(StringUtils.isNotEmpty(EmptyStorageList) && EmptyStorageList.size()>0){
                Map<String,Object> map = dedicatedStorager(EmptyStorageList,jobTotalPackageQty_BU,list);
                list.addAll((List<StorageRuleDto>) map.get("list"));
                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
            }
        }

        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || proDate!=null){
            //获取混放库位
//            putawayMoveLineNo = storageDistributionRuleUtils.baseStorageService.findPutawayNo(warehouseId,materialId);
//            Map<String,Object> map = getCanPutawayEmptyStorageList(warehouseId,putawayMoveLineNo);
//            list = (List<StorageRuleDto>) map.get("list");
//            SART = map.get("SART")!=null?map.get("SART").toString():"ASC";
//            if(list.size()>0){
//                map = mixupsStorageRule(list,SART,jobTotalPackageQty_BU);
//                list = (List<StorageRuleDto>) map.get("list");
//                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
//            }

            //获取混放库位
            List<StorageRuleDto> MixedWithStorageList = MixedWithStorage(warehouseId,materialId);
            if(StringUtils.isNotEmpty(MixedWithStorageList) && MixedWithStorageList.size()>0){
                Map<String,Object> map = dedicatedStorager(MixedWithStorageList,jobTotalPackageQty_BU,list);
                list.addAll((List<StorageRuleDto>) map.get("list"));
                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
            }
        }
        return list;
    }

    /**
     * 获取可上架专用库位列表
     * @param warehouseId 仓库id
     * @param materialId 物料id
     * @return 可上架列表
     */
    private static List<StorageRuleDto> getCanPutawayStorageList(Long warehouseId,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        map.put("orgId",sysUser.getOrganizationId());
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findStorageMaterial(map);
        list = calculateStorage(storageRuleDtos,materialId);
        return list;
    }

    /**
     * 获取批次相同库位列表
     * @param warehouseId
     * @param batchCode
     * @param prodDate
     * @return
     */
    private static List<StorageRuleDto> getBatchEqualStorageList(Long warehouseId, String batchCode, String prodDate,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("batchCode",batchCode);
        map.put("materialId",materialId);
        map.put("productionDate",prodDate);
        map.put("orgId",sysUser.getOrganizationId());
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.BatchEqualStorage(map);
        list = calculateStorage(storageRuleDtos,materialId);
        return list;
    }

   private static List<StorageRuleDto> getCanPutawayEmptyStorageList(Long warehouseId,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
       map.put("orgId",sysUser.getOrganizationId());
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.EmptyStorage(map);
        list = calculateStorage(storageRuleDtos,materialId);
        return list;
    }

    /**
     * 空库位
     * @param warehouseId
     * @param materialId
     * @return
     */
    private static List<StorageRuleDto> LasetStorage(Long warehouseId,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        map.put("orgId",sysUser.getOrganizationId());
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.LastStorage(map);
        list = calculateStorage(storageRuleDtos,materialId);
        return list;
    }

    /**
     * 混放库位
     * @param warehouseId
     * @param materialId
     * @return
     */
    private static List<StorageRuleDto> MixedWithStorage(Long warehouseId,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        map.put("orgId",sysUser.getOrganizationId());
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.MixedWithStorage(map);
        list = calculateStorage(storageRuleDtos,materialId);
        return list;
    }

    /**
     * 获取可上架空库位列表 /混合
     * @param warehouseId
     * @param putawayMoveLineNo 上架库位动线号
     * @return 库位列表
     */
    private static Map<String,Object> getCanPutawayEmptyStorageList(Long warehouseId,Integer putawayMoveLineNo,Long materialId){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<StorageRuleDto> list = new ArrayList<>();
        List<StorageRuleDto> storageRuleDtos = null;
        Map<String,Object> mapList = new HashMap<>();
        if(StringUtils.isNotEmpty(putawayMoveLineNo)){
            //生成随机数
            int max=10,min=1;
            int ran2 = (int) (Math.random()*(max-min)+min);
            //基准动线号排序：DESC-降序、ASC-升序
            String SART = "ASC";
            Map<String,Object> map = new HashMap<>();
            map.put("warehouseId",warehouseId);
            map.put("putawayMoveLineNo",putawayMoveLineNo);
            map.put("orgId",sysUser.getOrganizationId());
            if(ran2<=5){
                //降序
                map.put("SART","DESC");
                storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
                if(StringUtils.isEmpty(storageRuleDtos)){
                    map.put("SART","ASC");
                    storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
                }
            }else{
                //升序
                map.put("SART","ASC");
                storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
                if(StringUtils.isEmpty(storageRuleDtos)){
                    map.put("SART","DESC");
                    storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
                }
            }
            mapList.put("SART",map.get(SART));
        }
        if(StringUtils.isEmpty(storageRuleDtos)||StringUtils.isEmpty(putawayMoveLineNo)){
            Map<String,Object> map = new HashMap<>();
            map.put("warehouseId",warehouseId);
            storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
            list = calculateStorage(storageRuleDtos,materialId);
        }
        mapList.put("list",list);
        return mapList;
    }

    private static List<StorageRuleDto> getCanMixtureStorageList(){
        return null;
    }

    /**
     * 计算库位可放数量
     * @param storageRuleDtos
     * @return
     */
    private static List<StorageRuleDto> calculateStorage(List<StorageRuleDto> storageRuleDtos,Long materialId){
        List<StorageRuleDto> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            for (StorageRuleDto storageRuleDto : storageRuleDtos) {

                //获取配置项
                SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                searchSysSpecItem.setSpecCode("capacity");
                List<SysSpecItem> itemList = storageDistributionRuleUtils.securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

                //原始库位容量规则
                if(itemList.size()<1 || StringUtils.isEmpty(itemList.get(0).getParaValue()) || itemList.get(0).getParaValue().equals("base_storage")){
                    if(StringUtils.isEmpty(storageRuleDto.getVolume(),storageRuleDto.getNetWeight())){
                        throw new BizErrorException("请维护物料体积重量");
                    }
                    //物料体积必须大于0
                    if(storageRuleDto.getVolume().compareTo(BigDecimal.ZERO)==1 && storageRuleDto.getNetWeight().compareTo(BigDecimal.ZERO)==1){
                        //库位按体积可上架数 = 剩余体积/物料体积 向下去整数
                        storageRuleDto.setVolumeQty(storageRuleDto.getSurplusVolume().divide(storageRuleDto.getVolume(),0,BigDecimal.ROUND_DOWN));
                        storageRuleDto.setNetWeightQty(storageRuleDto.getSurplusLoad().divide(storageRuleDto.getNetWeight(),0,BigDecimal.ROUND_DOWN));
                        if(storageRuleDto.getVolumeQty().compareTo(storageRuleDto.getNetWeightQty())==1){
                            storageRuleDto.setPutawayQty(storageRuleDto.getNetWeightQty());
                        }else{
                            storageRuleDto.setPutawayQty(storageRuleDto.getVolumeQty());
                        }
                        if(storageRuleDto.getPutawayQty().compareTo(BigDecimal.ZERO)==1){
                            list.add(storageRuleDto);
                        }
                    }else{
                        throw new BizErrorException("请维护物料体积重量");
                    }
                }else {

                    if("base_storage_capacity".equals(itemList.get(0).getParaValue())){
                        BigDecimal totalQty = storageDistributionRuleUtils.calculate(storageRuleDto.getStorageId(),materialId);
                        if(StringUtils.isNotEmpty(totalQty) && totalQty.compareTo(BigDecimal.ZERO)==1){
                            storageRuleDto.setPutawayQty(totalQty);
                            list.add(storageRuleDto);
                        }
                    }

                    //库容规则
                    if ("1base_storage_capacity".equals(itemList.get(0).getParaValue())) {
                        //通过库位获取库位储存类型A、B、C、D类
                        //再通过物料获取A、B、C、D类库容
                        List<BaseStorage> storages = storageDistributionRuleUtils.baseStorageService.findList(ControllerUtil.dynamicCondition("storageId",storageRuleDto.getStorageId()));
                        if(storages.isEmpty()){
                            throw new BizErrorException("获取库位信息失败");
                        }
                        List<BaseStorageCapacity> baseStorageCapacities = storageDistributionRuleUtils.baseStorageCapacityService.findList(ControllerUtil.dynamicCondition("materialId",materialId));
                        //获取库存数量
                        BigDecimal totalQty = storageDistributionRuleUtils.baseStorageCapacityService.totalQty(ControllerUtil.dynamicCondition("storageId",storageRuleDto.getStorageId(),"materialId",materialId));
                        //计算上架已分配的数量
                        BigDecimal putJobQty = storageDistributionRuleUtils.baseStorageCapacityService.putJobQty(ControllerUtil.dynamicCondition("storageId",storageRuleDto.getStorageId(),"materialId",materialId));
                        if(StringUtils.isNotEmpty(putJobQty)){
                            totalQty.add(putJobQty);
                        }
                        if(StringUtils.isEmpty(totalQty)){
                            totalQty = BigDecimal.ZERO;
                        }



                        //混放 A 货品可存放数量 = 单个货品库存数/库容数量*A货品

                        if(baseStorageCapacities.size()>0) {
                            if (StringUtils.isNotEmpty(storages.get(0).getMaterialStoreType())) {
                                switch (storages.get(0).getMaterialStoreType()) {
                                    case 1:
                                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeACapacity())) {
                                            throw new BizErrorException("未维护A类容量");
                                        }
                                        //计算剩余容量 = 总容量-库存数量
                                        totalQty = baseStorageCapacities.get(0).getTypeACapacity().subtract(totalQty);
                                        if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                                            storageRuleDto.setPutawayQty(totalQty);
                                            list.add(storageRuleDto);
                                        }
                                        break;
                                    case 2:
                                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeBCapacity())) {
                                            throw new BizErrorException("未维护B类容量");
                                        }
                                        totalQty = baseStorageCapacities.get(0).getTypeBCapacity().subtract(totalQty);
                                        if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                                            storageRuleDto.setPutawayQty(totalQty);
                                            list.add(storageRuleDto);
                                        }
                                        break;
                                    case 3:
                                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeCCapacity())) {
                                            throw new BizErrorException("未维护C类容量");
                                        }
                                        totalQty = baseStorageCapacities.get(0).getTypeCCapacity().subtract(totalQty);
                                        if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                                            storageRuleDto.setPutawayQty(totalQty);
                                            list.add(storageRuleDto);
                                        }
                                        break;
                                    case 4:
                                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeDCapacity())) {
                                            throw new BizErrorException("未维护D类容量");
                                        }
                                        totalQty = baseStorageCapacities.get(0).getTypeDCapacity().subtract(totalQty);
                                        if (totalQty.compareTo(BigDecimal.ZERO) == 1) {
                                            storageRuleDto.setPutawayQty(totalQty);
                                            list.add(storageRuleDto);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 专用库位分配子规则 and 批次规则相同分配子规则
     * @param storageRuleDtos 可上架库位集合
     * @return 专用库位列表及可上架数量
     */
    private static Map<String,Object> dedicatedStorager(List<StorageRuleDto> storageRuleDtos,BigDecimal jobTotalPackageQty_BU,List<StorageRuleDto> allList){
        Map<String,Object>map = new HashMap<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            //按剩余承载量、体积排序
            storageRuleDtos.stream()
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusLoad))
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusVolume))
                    .collect(Collectors.toList());
            map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU,allList);
        }
        return map;
    }

    /**
     * 空库位分配子规则
     * @param storageRuleDtos
     * @param SART
     * @param jobTotalPackageQty_BU
     * @return
     */
    private static Map<String, Object> EmptyStorageRule(List<StorageRuleDto> storageRuleDtos, String SART, BigDecimal jobTotalPackageQty_BU,List<StorageRuleDto> allList){
        //上架动线号升序
        storageRuleDtos.stream()
                .sorted(Comparator.comparing(StorageRuleDto::getPutawayMoveLineNo))
                .collect(Collectors.toList());
        if(SART.equals("DESC")){
            //降序
            Collections.reverse(storageRuleDtos);
        }
        Map<String ,Object> map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU,allList);
        return map;
    }

    /**
     * 混放库位分配子规则
     * @param storageRuleDtos
     * @param SART
     * @param jobTotalPackageQty_BU
     * @return
     */
    private static Map<String ,Object> mixupsStorageRule(List<StorageRuleDto> storageRuleDtos,String SART,BigDecimal jobTotalPackageQty_BU,List<StorageRuleDto> allList){
        //剩余载重、剩余容积、上架动线号升序
        storageRuleDtos.stream()
                .sorted(Comparator.comparing(StorageRuleDto::getSurplusLoad))
                .sorted(Comparator.comparing(StorageRuleDto::getSurplusVolume))
                .sorted(Comparator.comparing(StorageRuleDto::getPutawayMoveLineNo))
                .collect(Collectors.toList());
        if(SART.equals("DESC")){
            //降序
            Collections.reverse(storageRuleDtos);
        }
        Map<String ,Object> map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU,allList);
        return map;
    }

    private static Map<String,Object> calculateStorageRule(List<StorageRuleDto> storageRuleDtos,BigDecimal jobTotalPackageQty_BU,List<StorageRuleDto> allLists){
        Map<String ,Object> map = new HashMap<>();
        List<StorageRuleDto> list = new ArrayList<>();
        //去重复
        storageRuleDtos = storageDistributionRuleUtils.removeList(storageRuleDtos,allLists);
        for (StorageRuleDto storageRuleDto : storageRuleDtos) {
            if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){
//                if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())==1){
//                    storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
//                    list.add(storageRuleDto);
//                    jobTotalPackageQty_BU = BigDecimal.ZERO;
//                }
//                if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())<1){
//                    storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
//                    list.add(storageRuleDto);
//                    jobTotalPackageQty_BU = jobTotalPackageQty_BU.subtract(storageRuleDto.getPutawayQty());
//                }
                if(storageRuleDto.getPutawayQty().compareTo(jobTotalPackageQty_BU)<1){
                    list.add(storageRuleDto);
                    jobTotalPackageQty_BU = jobTotalPackageQty_BU.subtract(storageRuleDto.getPutawayQty());
                }else{
                    storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
                    list.add(storageRuleDto);
                    jobTotalPackageQty_BU = jobTotalPackageQty_BU.subtract(storageRuleDto.getPutawayQty());
                }
            }
        }
        map.put("list",list);
        map.put("jobTotalPackageQty_BU",jobTotalPackageQty_BU);
        return map;
    }

    private List<StorageRuleDto> removeList(List<StorageRuleDto> list1,List<StorageRuleDto> list2){
        List<StorageRuleDto> list = new ArrayList<>();
        if(list2.size()>0){
            for (StorageRuleDto storageRuleDto : list1) {
                for (StorageRuleDto ruleDto : list2) {
                    if(!Objects.equals(storageRuleDto.getStorageId(), ruleDto.getStorageId())){
                        list.add(storageRuleDto);
                    }
                }
            }
        }else {
            return list1;
        }
        return list;
    }


    /**
     * 库容比例计算
     * @return
     */
    private BigDecimal calculate(Long storageId,Long materialId){
        List<BaseStorage> storages = storageDistributionRuleUtils.baseStorageService.findList(ControllerUtil.dynamicCondition("storageId",storageId));
        if(storages.isEmpty()){
            throw new BizErrorException("获取库位信息失败");
        }
        //获取物料编码并截取前八位数
        BaseMaterial baseMaterialList = storageDistributionRuleUtils.baseMaterialService.findList(ControllerUtil.dynamicCondition("materialId",materialId)).get(0);
        if(baseMaterialList.getMaterialCode().length()<8){
            throw new BizErrorException("物料编码错误，长度小于规定8位，无法匹配库容");
        }
        String strMaterialCode = baseMaterialList.getMaterialCode().substring(0,8);
        List<BaseStorageCapacity> baseStorageCapacities = storageDistributionRuleUtils.baseStorageCapacityService.findList(ControllerUtil.dynamicCondition("CodePrefix",strMaterialCode));

        //查询库位下的所以货品及数量
        List<WmsInnerInventory> wmsInnerInventories = storageDistributionRuleUtils.baseStorageCapacityService.wmsList(ControllerUtil.dynamicCondition("storageId",storageId));
        //
        //已知货品A在A仓库可以放10个货品B在A仓库可以放20个 现A仓库有货品A 5个 货品B 4个 求货品A跟货品B还可以在仓库A各存放多少个
        //B货品可存放量 = 5/10*20=10 20-10=10    A货品可存放量 = 4/20*10=2
        //可存放数量
        BigDecimal totalQty = BigDecimal.ZERO;
        if(baseStorageCapacities.size()>0) {
            if (StringUtils.isNotEmpty(storages.get(0).getMaterialStoreType())) {

                BigDecimal TypeCapacity = BigDecimal.ZERO;
                byte type = 0;
                switch (storages.get(0).getMaterialStoreType()){
                    case 1:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeACapacity())) {
                            throw new BizErrorException("未维护A类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeACapacity();
                        type = 1;
                        break;
                    case 2:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeBCapacity())) {
                            throw new BizErrorException("未维护B类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeBCapacity();
                        type = 2;
                        break;
                    case 3:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeCCapacity())) {
                            throw new BizErrorException("未维护C类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeCCapacity();
                        type = 3;
                        break;
                    case 4:
                        if (StringUtils.isEmpty(baseStorageCapacities.get(0).getTypeDCapacity())) {
                            throw new BizErrorException("未维护D类容量");
                        }
                        TypeCapacity = baseStorageCapacities.get(0).getTypeDCapacity();
                        type = 4;
                        break;
                }

                if(wmsInnerInventories.size()>0){
                    BigDecimal qty = baseStorageCapacities.get(0).getTypeACapacity();
                    for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
                        if(!Objects.equals(wmsInnerInventory.getMaterialId(), materialId)){

                            //获取物料编码并截取前八位数
                            BaseMaterial baseMaterial = storageDistributionRuleUtils.baseMaterialService.findList(ControllerUtil.dynamicCondition("materialId",wmsInnerInventory.getMaterialId())).get(0);
                            if(baseMaterialList.getMaterialCode().length()<8){
                                throw new BizErrorException("物料编码错误，长度小于规定8位，无法匹配库容");
                            }
                            String substring = baseMaterial.getMaterialCode().substring(0,8);

                            //转换数量
                            //查询该货品库容
                            List<BaseStorageCapacity> shiftStorageCapacity = storageDistributionRuleUtils.baseStorageCapacityService.findList(ControllerUtil.dynamicCondition("CodePrefix",substring));
                            if(shiftStorageCapacity.isEmpty()){
                                break;
                            }
                            BigDecimal shiftCapacity = BigDecimal.ZERO;
                            if(StringUtils.isNotEmpty(shiftStorageCapacity)) {
                                if (type == 1) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeACapacity();
                                } else if (type == 2) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeBCapacity();
                                } else if (type == 3) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeCCapacity();
                                } else if (type == 4) {
                                    shiftCapacity = shiftStorageCapacity.get(0).getTypeDCapacity();
                                }
                            }
                            //库存数/转换货品库容*货品库容
                            BigDecimal a = wmsInnerInventory.getPackingQty().divide(shiftCapacity,2).multiply(TypeCapacity);
                            //四舍五入取整数
                            a = a.setScale(0, RoundingMode.HALF_UP);
                            qty = qty.subtract(a);
                        }else {
                            qty = qty.subtract(wmsInnerInventory.getPackingQty());
                        }
                    }
                    if(qty.compareTo(BigDecimal.ZERO)==1){
                        totalQty = qty;
                    }
                }else {
                    totalQty = baseStorageCapacities.get(0).getTypeACapacity();
                }
            }
        }

        return totalQty;
    }
}
