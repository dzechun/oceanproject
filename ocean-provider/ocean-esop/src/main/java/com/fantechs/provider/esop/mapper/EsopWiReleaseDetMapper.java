package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.EsopWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiReleaseDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EsopWiReleaseDetMapper extends MyMapper<EsopWiReleaseDet> {
    List<EsopWiReleaseDetDto> findList(SearchEsopWiReleaseDet searchEsopWiReleaseDet);
}