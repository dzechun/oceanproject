package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.history.QmsHtPdaInspection;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtPdaInspectionMapper extends MyMapper<QmsHtPdaInspection> {
    List<QmsHtPdaInspection> findHtList(Map<String, Object> map);
}
