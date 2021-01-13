package com.fantechs.provider.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtStockDto;
import com.fantechs.common.base.entity.apply.SmtStock;
import com.fantechs.common.base.entity.apply.search.SearchSmtStock;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtStockMapper extends MyMapper<SmtStock> {
    List<SmtStockDto> findList(SearchSmtStock searchSmtStock);
}