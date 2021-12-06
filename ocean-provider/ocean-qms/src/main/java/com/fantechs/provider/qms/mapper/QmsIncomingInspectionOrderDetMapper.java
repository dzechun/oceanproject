package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIncomingInspectionOrderDetMapper extends MyMapper<QmsIncomingInspectionOrderDet> {
    List<QmsIncomingInspectionOrderDetDto> findList(Map<String, Object> map);
}