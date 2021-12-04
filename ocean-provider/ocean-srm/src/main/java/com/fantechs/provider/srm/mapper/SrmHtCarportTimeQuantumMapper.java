package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtCarportTimeQuantumDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtCarportTimeQuantum;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtCarportTimeQuantumMapper extends MyMapper<SrmHtCarportTimeQuantum> {
    List<SrmHtCarportTimeQuantumDto> findList(Map<String, Object> map);
}