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
import java.util.ArrayList;
import java.util.List;

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
    public void inti(){
        storageDistributionRuleUtils = this;
        storageDistributionRuleUtils.baseStorageService = this.baseStorageService;
    }

    /**
     * 上架分配主规则
     * @param packageQty
     * @return
     */
    public static List<T> JobMainRule(BigDecimal packageQty){
        //可用库位列表
        List<T> list = new ArrayList<>();
        //待上架总数包装数量
        BigDecimal jobTotalPackageQty_BU = packageQty;
        //基准动线号排序：DOWN-降序、ASC-升序
        String SART = "ASC";
        if(jobTotalPackageQty_BU.compareTo(BigDecimal.ZERO)==1){

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
        List<StorageRuleDto> storageRuleDtos = storageDistributionRuleUtils.baseStorageService.findPutawayRule(warehouseId,materialId);
        if(storageRuleDtos.size()>0){
            for (StorageRuleDto storageRuleDto : storageRuleDtos) {
                if(StringUtils.isEmpty(storageRuleDto.getVolume(),storageRuleDto.getNetWeight())){
                    throw new BizErrorException("请维护物料体积重量");
                }
                //库位按体积可上架数 = 剩余体积/物料体积 向下去整数
                storageRuleDto.setVolumeQty(storageRuleDto.getSurplusVolume().divide(storageRuleDto.getVolume(),0,BigDecimal.ROUND_DOWN));
                storageRuleDto.setNetWeightQty(storageRuleDto.getSurplusLoad().divide(storageRuleDto.getNetWeight(),0,BigDecimal.ROUND_DOWN));

            }
        }
        return list;
    }
}
