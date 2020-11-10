package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSpecDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleSpecMapper extends MyMapper<SmtBarcodeRuleSpec> {
    List<SmtBarcodeRuleSpecDto> findList(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec);

    int updateBatch(List<SmtBarcodeRuleSpec> list);
}