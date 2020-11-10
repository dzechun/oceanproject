package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleSetMapper extends MyMapper<SmtBarcodeRuleSet> {
    List<SmtBarcodeRuleSetDto> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}