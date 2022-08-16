package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseCurrencyDto;
import com.fantechs.common.base.general.entity.basic.BaseCurrency;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseCurrencyMapper extends MyMapper<BaseCurrency> {

    List<BaseCurrencyDto> findList(Map<String, Object> map);
}