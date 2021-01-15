package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtStockDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStock;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtStockMapper extends MyMapper<SmtStock> {
    List<SmtStockDto> findList(SearchSmtStock searchSmtStock);
}