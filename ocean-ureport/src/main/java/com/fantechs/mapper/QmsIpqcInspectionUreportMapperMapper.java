package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.entity.QmsIpqcDtaticElectricityModel;
import com.fantechs.entity.QmsIpqcSamplingModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionUreportMapperMapper extends MyMapper<QmsIpqcInspectionOrder> {
    List<QmsIpqcDtaticElectricityModel> findDtaticElectricityList(Map<String, Object> map);

    List<QmsIpqcSamplingModel> findSamplingList(Map<String, Object> map);

}