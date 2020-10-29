package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleMapper extends MyMapper<SmtBarcodeRule> {
    List<SmtBarcodeRuleDto> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}