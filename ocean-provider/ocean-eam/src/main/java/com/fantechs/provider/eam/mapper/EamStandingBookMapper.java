package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamStandingBookMapper extends MyMapper<EamStandingBook> {
    List<EamStandingBookDto> findList(Map<String,Object> map);
}