package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsIpqcInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionOrderDetMapper extends MyMapper<QmsIpqcInspectionOrderDet> {
    List<QmsIpqcInspectionOrderDet> findDetList(Map<String, Object> map);

    List<QmsIpqcInspectionOrderDetDto> findList(Map<String, Object> map);
}