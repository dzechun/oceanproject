package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleSetDetMapper extends MyMapper<SmtBarcodeRuleSetDet> {
    List<SmtBarcodeRuleSetDetDto> findList(SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet);
}