package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsIncomingInspectionOrderMapper extends MyMapper<QmsIncomingInspectionOrder> {
    List<QmsIncomingInspectionOrderDto> findList(Map<String, Object> map);

    int batchUpdate(List<QmsIncomingInspectionOrder> list);
}