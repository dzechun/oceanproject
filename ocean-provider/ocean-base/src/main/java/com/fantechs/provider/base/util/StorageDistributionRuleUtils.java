package com.fantechs.provider.base.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseStorageService;
import org.apache.poi.ss.formula.functions.T;
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
    public static List<StorageRuleDto> JobMainRule(BigDecimal packageQty,Long warehouseId,Long materialId,Integer putawayMoveLineNo){
        //可用库位列表
        List<StorageRuleDto> list = new ArrayList<>();
        //待上架总数包装数量
        BigDecimal jobTotalPackageQty_BU = packageQty;
        //基准动线号排序：DOWN-降序、ASC-升序
        String SART = "ASC";
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){
            //获取货品专用库位
            list = storageDistributionRuleUtils.getCanPutawayStorageList(warehouseId,materialId);
            if(StringUtils.isNotEmpty(list) && list.size()>0){
                //专用库位分配子规则
                //
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
    private List<StorageRuleDto> getCanPutawayStorageList(Long warehouseId,Long materialId){
        List<StorageRuleDto> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("materialId",materialId);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
        list = storageDistributionRuleUtils.calculateStorage(storageRuleDtos);
//        if(storageRuleDtos.size()>0){
//            for (StorageRuleDto storageRuleDto : storageRuleDtos) {
//                if(StringUtils.isEmpty(storageRuleDto.getVolume(),storageRuleDto.getNetWeight())){
//                    throw new BizErrorException("请维护物料体积重量");
//                }
//                //库位按体积可上架数 = 剩余体积/物料体积 向下去整数
//                storageRuleDto.setVolumeQty(storageRuleDto.getSurplusVolume().divide(storageRuleDto.getVolume(),0,BigDecimal.ROUND_DOWN));
//                storageRuleDto.setNetWeightQty(storageRuleDto.getSurplusLoad().divide(storageRuleDto.getNetWeight(),0,BigDecimal.ROUND_DOWN));
//                if(storageRuleDto.getVolumeQty().compareTo(storageRuleDto.getNetWeightQty())==1){
//                    storageRuleDto.setPutawayQty(storageRuleDto.getNetWeightQty());
//                }else{
//                    storageRuleDto.setPutawayQty(storageRuleDto.getVolumeQty());
//                }
//                if(storageRuleDto.getPutawayQty().compareTo(BigDecimal.ZERO)==1){
//                    list.add(storageRuleDto);
//                }
//            }
//        }
        return list;
    }

    /**
     * 获取批次相同库位列表
     * @param warehouseId
     * @param batchCode
     * @param prodDate
     * @return
     */
    private List<StorageRuleDto> getBatchEqualStorageList(Long warehouseId, String batchCode, Date prodDate){
        List<StorageRuleDto> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",warehouseId);
        map.put("batchCode",batchCode);
        map.put("prodDate",prodDate);
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
        list = storageDistributionRuleUtils.calculateStorage(storageRuleDtos);
        return list;
    }

    /**
     * 获取可上架空库位列表 /混合
     * @param warehouseId
     * @param putawayMoveLineNo 上架库位动线号
     * @return 库位列表
     */
    private List<StorageRuleDto> getCanPutawayEmptyStorageList(Long warehouseId,Integer putawayMoveLineNo){
        List<StorageRuleDto> list = new ArrayList<>();
        List<StorageRuleDto> storageRuleDtos = null;
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
        }
        if(StringUtils.isEmpty(storageRuleDtos)||StringUtils.isEmpty(putawayMoveLineNo)){
            Map<String,Object> map = new HashMap<>();
            map.put("warehouseId",warehouseId);
            storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(map);
            list = storageDistributionRuleUtils.calculateStorage(storageRuleDtos);
        }
        return list;
    }

    private List<StorageRuleDto> getCanMixtureStorageList(){
        return null;
    }

    private List<StorageRuleDto> calculateStorage(List<StorageRuleDto> storageRuleDtos){
        List<StorageRuleDto> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            for (StorageRuleDto storageRuleDto : storageRuleDtos) {
                if(StringUtils.isEmpty(storageRuleDto.getVolume(),storageRuleDto.getNetWeight())){
                    throw new BizErrorException("请维护物料体积重量");
                }
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
            }
        }
        return list;
    }

    /**
     * 专用库位分配子规则
     * @param storageRuleDtos 可上架库位集合
     * @return 专用库位列表
     */
    private List<StorageRuleDto> dedicatedStorager(List<StorageRuleDto> storageRuleDtos,BigDecimal jobTotalPackageQty_BU){
        List<StorageRuleDto> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(storageRuleDtos)&&storageRuleDtos.size()>0){
            //按剩余承载量、体积排序
            storageRuleDtos.stream()
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusLoad))
                    .sorted(Comparator.comparing(StorageRuleDto::getSurplusVolume))
                    .collect(Collectors.toList());
            for (StorageRuleDto storageRuleDto : storageRuleDtos) {
                if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){
                    if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())>1){
                        storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
                        list.add(storageRuleDto);
                        jobTotalPackageQty_BU = BigDecimal.ZERO;
                    }
                    if(jobTotalPackageQty_BU.compareTo(storageRuleDto.getPutawayQty())==1){
                        storageRuleDto.setPutawayQty(jobTotalPackageQty_BU);
                        list.add(storageRuleDto);
                        jobTotalPackageQty_BU = jobTotalPackageQty_BU.subtract(storageRuleDto.getPutawayQty());
                    }
                }
            }
        }
        return list;
    }
}
