package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionOrderMapper extends MyMapper<QmsIpqcInspectionOrder> {
    List<QmsIpqcInspectionOrder> findList(Map<String, Object> map);
}