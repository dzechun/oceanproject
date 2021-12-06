package com.fantechs.provider.qms.mapper;


import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtIncomingInspectionOrderMapper extends MyMapper<QmsHtIncomingInspectionOrder> {
    List<QmsHtIncomingInspectionOrder> findHtList(Map<String, Object> map);
}