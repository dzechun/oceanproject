package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopNewsDto;
import com.fantechs.common.base.general.entity.esop.EsopNews;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopNewsMapper extends MyMapper<EsopNews> {
    List<EsopNewsDto> findList(Map<String,Object> map);
}