package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseLabelMapper extends MyMapper<BaseLabel> {

    List<BaseLabelDto> findList(SearchBaseLabel searchBaseLabel);
}