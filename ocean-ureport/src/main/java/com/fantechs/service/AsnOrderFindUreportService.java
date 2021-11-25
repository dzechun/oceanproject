package com.fantechs.service;

import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface AsnOrderFindUreportService extends IService<SrmAsnOrder> {
    List<SrmAsnOrder> findList(Map<String, Object> map);
}
