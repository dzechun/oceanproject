package com.fantechs.service;

import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.OmPurchaseOrderUreport;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SupplierUreportService  extends IService<BaseSupplierInfo> {
    List<BaseSupplierInfo> findList(Map<String, Object> map);
}
