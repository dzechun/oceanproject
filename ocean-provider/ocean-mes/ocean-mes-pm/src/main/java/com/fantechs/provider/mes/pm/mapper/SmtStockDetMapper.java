package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtStockDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStockDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtStockDetMapper extends MyMapper<SmtStockDet> {
    List<SmtStockDetDto> findList(SearchSmtStockDet searchSmtStockDet);

    int updateBatch(List<SmtStockDet> list);
}