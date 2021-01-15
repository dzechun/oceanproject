package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderBarcodeCollocationMapper extends MyMapper<SmtWorkOrderBarcodeCollocation> {
    List<SmtWorkOrderBarcodeCollocationDto> findList(SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation);
}