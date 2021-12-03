package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.support.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */

public interface QmsIpqcInspectionOrderService extends IService<QmsIpqcInspectionOrder> {
    List<QmsIpqcInspectionOrder> findList(Map<String, Object> map);
    int writeBack(Long ipqcInspectionOrderId);
    String uploadFile(MultipartFile file);
    QmsIpqcInspectionOrder createOrder( SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder);
    int PDASubmit(QmsIpqcInspectionOrder qmsIpqcInspectionOrder);
    QmsIpqcInspectionOrder selectByKey(Long key);
}
