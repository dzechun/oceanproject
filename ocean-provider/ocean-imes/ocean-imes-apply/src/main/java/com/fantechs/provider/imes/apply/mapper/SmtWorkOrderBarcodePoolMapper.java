package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWorkOrderBarcodePoolMapper extends MyMapper<SmtWorkOrderBarcodePool> {
    List<SmtWorkOrderBarcodePool> findList(SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool);
}