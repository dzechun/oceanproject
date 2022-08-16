package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionOrderDetMapper extends MyMapper<QmsInspectionOrderDet> {
    List<QmsInspectionOrderDet> findDetList(Map<String, Object> map);
}