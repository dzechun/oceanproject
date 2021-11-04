package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.entity.eng.EngLogisticsRecord;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngLogisticsRecordMapper extends MyMapper<EngLogisticsRecord> {
    List<EngLogisticsRecord> findList(Map<String, Object> map);
}