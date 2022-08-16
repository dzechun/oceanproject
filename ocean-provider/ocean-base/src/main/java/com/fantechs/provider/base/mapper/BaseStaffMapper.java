package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseStaffDto;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseStaffMapper extends MyMapper<BaseStaff> {

    List<BaseStaffDto> findList(Map<String, Object> map);
}