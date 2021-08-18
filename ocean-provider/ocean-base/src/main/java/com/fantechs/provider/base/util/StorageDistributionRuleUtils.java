package com.fantechs.provider.base.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
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

    //声明对象
    private static StorageDistributionRuleUtils storageDistributionRuleUtils;

    //初始化
    @PostConstruct
    public void init(){
        storageDistributionRuleUtils = this;
        storageDistributionRuleUtils.baseStorageService = this.baseStorageService;
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
                Map<String ,Object> map = dedicatedStorager(CanPutawayStorageList,jobTotalPackageQty_BU);
                list.addAll((List<StorageRuleDto>) map.get("list"));
                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
            }
        }
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || batchCode!=null){
            //获取批次相同库位
            if(StringUtils.isNotEmpty(batchCode)){
                List<StorageRuleDto>  BatchEqualStorageList = getBatchEqualStorageList(warehouseId,batchCode,proDate,materialId);
                if(StringUtils.isNotEmpty(BatchEqualStorageList) && BatchEqualStorageList.size()>0){
                    Map<String,Object> map = dedicatedStorager(BatchEqualStorageList,jobTotalPackageQty_BU);
                    list.addAll((List<StorageRuleDto>) map.get("list"));
                    jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
                }
            }
        }

        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1 || proDate!=null){
            //获取空库位
            //putawayMoveLineNo = storageDistributionRuleUtils.baseStorageService.findPutawayNo(warehouseId,materialId);
//            Map<String,Object> map = getCanPutawayEmptyStorageList(warehouseId,putawayMoveLineNo);
//            list = (List<StorageRuleDto>) map.get("list");
//            SART = map.get("SART")!=null?map.get("SART").toString():"ASC";
//            if(list.size()>0){
//                map = EmptyStorageRule(list,SART,jobTotalPackageQty_BU);
//                list = (List<StorageRuleDto>) map.get("list");
//                jobTotalPackageQty_BU = (BigDecimal) map.get("jobTotalPackageQty_BU");
//            }
            List<StorageRuleDto> EmptyStorageList = getCanPutawayEmptyStorageList(warehouseId,materialId);
            if(StringUtils.isNotEmpty(EmptyStorageList) && EmptyStorageList.size()>0){
                Map<String,Object> map = dedicatedStorager(EmptyStorageList,jobTotalPackageQty_BU);
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
            List<StorageRuleDto> MixedWithStorageList = MixedWithStorage(warehouseId,materialId);
            if(StringUtils.isNotEmpty(MixedWithStorageList) && MixedWithStorageList.size()>0){
                Map<String,Object> map = dedicatedStorager(MixedWithStorageList,jobTotalPackageQty_BU);
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
        List<StorageRuleDto> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findStorageMaterial(map);
        list = calculateStorage(storageRuleDtos);
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
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("batchCode",batchCode);
        map.put("materialId",materialId);
        map.put("productionDate",prodDate);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.BatchEqualStorage(map);
        list = calculateStorage(storageRuleDtos);
        return list;
    }

    private static List<StorageRuleDto> getCanPutawayEmptyStorageList(Long warehouseId,Long materialId){
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.EmptyStorage(map);
        list = calculateStorage(storageRuleDtos);
        return list;
    }

    private static List<StorageRuleDto> MixedWithStorage(Long warehouseId,Long materialId){
        List<StorageRuleDto> list;
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.MixedWithStorage(map);
        list = calculateStorage(storageRuleDtos);
        return list;
    }

    /**
     * 获取可上架空库位列表 /混合
     * @param warehouseId
     * @param putawayMoveLineNo 上架库位动线号
     * @return 库位列表
     */
    private static Map<String,Object> getCanPutawayEmptyStorageList(Long warehouseId,Integer putawayMoveLineNo){
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
            list = calculateStorage(storageRuleDtos);
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
    private static List<StorageRuleDto> calculateStorage(List<StorageRuleDto> storageRuleDtos){
        List<StorageRuleDto> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            for (StorageRuleDto storageRuleDto : storageRuleDtos) {
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
            }
        }
        return list;
    }

    /**
     * 专用库位分配子规则 and 批次规则相同分配子规则
     * @param storageRuleDtos 可上架库位集合
     * @return 专用库位列表及可上架数量
     */
    private static Map<String,Object> dedicatedStorager(List<StorageRuleDto> storageRuleDtos,BigDecimal jobTotalPackageQty_BU){
        Map<String,Object>map = new HashMap<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            //按剩余承载量、体积排序
            storageRuleDtos.stream()
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusLoad))
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusVolume))
                    .collect(Collectors.toList());
            map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU);
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
    private static Map<String, Object> EmptyStorageRule(List<StorageRuleDto> storageRuleDtos, String SART, BigDecimal jobTotalPackageQty_BU){
        //上架动线号升序
        storageRuleDtos.stream()
                .sorted(Comparator.comparing(StorageRuleDto::getPutawayMoveLineNo))
                .collect(Collectors.toList());
        if(SART.equals("DESC")){
            //降序
            Collections.reverse(storageRuleDtos);
        }
        Map<String ,Object> map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU);
        return map;
    }

    /**
     * 混放库位分配子规则
     * @param storageRuleDtos
     * @param SART
     * @param jobTotalPackageQty_BU
     * @return
     */
    private static Map<String ,Object> mixupsStorageRule(List<StorageRuleDto> storageRuleDtos,String SART,BigDecimal jobTotalPackageQty_BU){
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
        Map<String ,Object> map = calculateStorageRule(storageRuleDtos,jobTotalPackageQty_BU);
        return map;
    }

    private static Map<String,Object> calculateStorageRule(List<StorageRuleDto> storageRuleDtos,BigDecimal jobTotalPackageQty_BU){
        Map<String ,Object> map = new HashMap<>();
        List<StorageRuleDto> list = new ArrayList<>();
        for (StorageRuleDto storageRuleDto : storageRuleDtos) {
            if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){
//                if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())==1){
//                    storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
//                    list.add(storageRuleDto);
//                    jobTotalPackageQty_BU = BigDecimal.ZERO;
//                }
                if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())<1){
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
}
