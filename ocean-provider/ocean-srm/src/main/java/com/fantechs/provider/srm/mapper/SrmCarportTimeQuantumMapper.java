package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmCarportTimeQuantumDto;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmCarportTimeQuantumMapper extends MyMapper<SrmCarportTimeQuantum> {
    List<SrmCarportTimeQuantumDto> findList(Map<String, Object> map);
}