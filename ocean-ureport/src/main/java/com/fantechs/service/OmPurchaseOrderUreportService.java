package com.fantechs.service;

import com.fantechs.common.base.general.entity.ureport.OmPurchaseOrderUreport;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface OmPurchaseOrderUreportService extends IService<OmPurchaseOrderUreport> {
    List<OmPurchaseOrderUreport> findList(Map<String, Object> map);
}
