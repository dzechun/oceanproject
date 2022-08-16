package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionOrderMapper extends MyMapper<QmsInspectionOrder> {
    List<QmsInspectionOrder> findList(Map<String, Object> map);
}