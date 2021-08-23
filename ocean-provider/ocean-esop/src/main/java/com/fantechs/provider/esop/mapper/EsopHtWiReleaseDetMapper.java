package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopHtWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiReleaseDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtWiReleaseDetMapper extends MyMapper<EsopHtWiReleaseDet> {
    List<EsopHtWiReleaseDetDto> findHtList(Map<String,Object> map);
}