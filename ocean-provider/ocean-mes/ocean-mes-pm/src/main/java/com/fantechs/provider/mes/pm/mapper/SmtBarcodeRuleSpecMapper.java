package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleSpecMapper extends MyMapper<SmtBarcodeRuleSpec> {
    List<SmtBarcodeRuleSpecDto> findList(SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec);

    int updateBatch(List<SmtBarcodeRuleSpec> list);
}