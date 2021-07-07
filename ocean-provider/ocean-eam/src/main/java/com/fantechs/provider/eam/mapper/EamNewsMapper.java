package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamNewsDto;
import com.fantechs.common.base.general.entity.eam.EamNews;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamNewsMapper extends MyMapper<EamNews> {
    List<EamNewsDto> findList(Map<String,Object> map);
}