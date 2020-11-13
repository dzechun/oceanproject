package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtCurrencyDto;
import com.fantechs.common.base.entity.basic.SmtCurrency;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtCurrencyMapper extends MyMapper<SmtCurrency> {

    List<SmtCurrencyDto> findList(Map<String, Object> map);
}