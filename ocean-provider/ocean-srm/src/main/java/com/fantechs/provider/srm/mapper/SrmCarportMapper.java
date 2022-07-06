package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmCarportDto;
import com.fantechs.common.base.general.entity.srm.SrmCarport;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmCarportMapper extends MyMapper<SrmCarport> {
    List<SrmCarportDto> findList(Map<String, Object> map);
}