package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionOrderAuditUserMapper extends MyMapper<QmsIpqcInspectionOrderAuditUser> {
    List<QmsIpqcInspectionOrderAuditUser> findList(Map<String, Object> map);
}