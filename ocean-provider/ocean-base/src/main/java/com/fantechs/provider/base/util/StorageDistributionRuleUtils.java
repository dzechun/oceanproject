package com.fantechs.provider.base.util;

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
}
