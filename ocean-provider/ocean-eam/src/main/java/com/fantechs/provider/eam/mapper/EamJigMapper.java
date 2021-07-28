package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMapper extends MyMapper<EamJig> {
    List<EamJigDto> findList(Map<String,Object> map);
}