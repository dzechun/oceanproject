package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSampleBeforeRecheck;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionOrderDetSampleBeforeRecheckMapper extends MyMapper<QmsInspectionOrderDetSampleBeforeRecheck> {
    List<QmsInspectionOrderDetSampleBeforeRecheck> findList(Map<String, Object> map);
}