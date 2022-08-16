package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseStaffProcessMapper extends MyMapper<BaseStaffProcess> {

    List<BaseStaffProcess> findList(Map<String, Object> map);

    List<BaseStaffProcess> findByStaffId(Long staffId);
}