package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionUreportDetMapper extends MyMapper<QmsIpqcInspectionOrderDet> {
    List<QmsIpqcInspectionOrderDet> findList(Map<String, Object> map);
}