package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtIpqcInspectionOrderDetMapper extends MyMapper<QmsHtIpqcInspectionOrderDet> {
    List<QmsHtIpqcInspectionOrderDet> findHtList(Map<String, Object> map);
}