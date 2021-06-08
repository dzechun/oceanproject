package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderAuditUserMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderAuditUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@Service
public class QmsIpqcInspectionOrderAuditUserServiceImpl extends BaseService<QmsIpqcInspectionOrderAuditUser> implements QmsIpqcInspectionOrderAuditUserService {

    @Resource
    private QmsIpqcInspectionOrderAuditUserMapper qmsIpqcInspectionOrderAuditUserMapper;

    @Override
    public List<QmsIpqcInspectionOrderAuditUser> findList(Map<String, Object> map) {
        return qmsIpqcInspectionOrderAuditUserMapper.findList(map);
    }

}
