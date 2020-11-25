package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtStockDetDto;
import com.fantechs.common.base.entity.apply.SmtStockDet;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtStockDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtStockDetMapper extends MyMapper<SmtStockDet> {
    List<SmtStockDetDto> findList(SearchSmtStockDet searchSmtStockDet);

    int updateBatch(List<SmtStockDet> list);
}