package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.entity.wanbao.history.QmsHtInspectionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtInspectionOrderMapper extends MyMapper<QmsHtInspectionOrder> {
    List<QmsHtInspectionOrder> findHtList(Map<String, Object> map);
}