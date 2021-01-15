package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderBarcodePoolMapper extends MyMapper<SmtWorkOrderBarcodePool> {
    List<SmtWorkOrderBarcodePoolDto> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool);
}