package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.entity.QmsIpqcDtaticElectricityModel;
import com.fantechs.entity.QmsIpqcFirstArticleModel;
import com.fantechs.entity.QmsIpqcProcessInspectionModel;
import com.fantechs.entity.QmsIpqcSamplingModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionUreportMapper extends MyMapper<QmsIpqcInspectionOrder> {
    List<QmsIpqcDtaticElectricityModel> findDtaticElectricityList(Map<String, Object> map);

    List<QmsIpqcSamplingModel> findSamplingList(Map<String, Object> map);

    List<QmsIpqcFirstArticleModel> findFirstArticleList(Map<String, Object> map);

    List<QmsIpqcProcessInspectionModel> findProcessInspectionList(Map<String, Object> map);
}