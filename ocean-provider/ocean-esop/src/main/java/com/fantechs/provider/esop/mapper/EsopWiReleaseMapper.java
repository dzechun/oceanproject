package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.EsopWiRelease;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiRelease;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EsopWiReleaseMapper extends MyMapper<EsopWiRelease> {
    List<EsopWiReleaseDto> findList(SearchEsopWiRelease searchEsopWiRelease);
}