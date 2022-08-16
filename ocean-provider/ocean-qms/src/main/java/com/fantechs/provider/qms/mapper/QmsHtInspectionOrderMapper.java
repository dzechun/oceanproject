package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtInspectionOrderMapper extends MyMapper<QmsHtInspectionOrder> {
    List<QmsHtInspectionOrder> findHtList(Map<String, Object> map);
}