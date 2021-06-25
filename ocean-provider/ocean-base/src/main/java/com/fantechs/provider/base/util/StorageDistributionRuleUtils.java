package com.fantechs.provider.base.util;

import com.fantechs.provider.base.service.BaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
     */
    //public static List<>
}
