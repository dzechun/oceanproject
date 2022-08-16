package com.fantechs.provider.qms.service;


import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */

public interface QmsIpqcInspectionOrderAuditUserService extends IService<QmsIpqcInspectionOrderAuditUser> {
    List<QmsIpqcInspectionOrderAuditUser> findList(Map<String, Object> map);
}
