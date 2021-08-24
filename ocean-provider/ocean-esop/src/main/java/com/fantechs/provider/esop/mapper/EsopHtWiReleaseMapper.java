package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiRelease;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtWiReleaseMapper extends MyMapper<EsopHtWiRelease> {
    List<EsopHtWiReleaseDto> findHtList(Map<String,Object> map);
}