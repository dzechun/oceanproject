package com.fantechs.provider.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderBarcodePoolMapper extends MyMapper<SmtWorkOrderBarcodePool> {
    List<SmtWorkOrderBarcodePoolDto> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool);
}