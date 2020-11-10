package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSetDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleSetDetMapper extends MyMapper<SmtBarcodeRuleSetDet> {
    List<SmtBarcodeRuleSetDetDto> findList(SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet);
}