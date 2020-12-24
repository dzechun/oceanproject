package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsSamplingPlan;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsSamplingPlanMapper extends MyMapper<QmsSamplingPlan> {
    List<QmsSamplingPlan> findList(Map<String, Object> map);
}
