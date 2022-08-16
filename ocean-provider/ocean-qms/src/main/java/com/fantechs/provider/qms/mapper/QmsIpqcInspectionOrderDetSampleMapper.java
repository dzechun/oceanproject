package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIpqcInspectionOrderDetSampleMapper extends MyMapper<QmsIpqcInspectionOrderDetSample> {
    List<QmsIpqcInspectionOrderDetSample> findList(Map<String, Object> map);
}