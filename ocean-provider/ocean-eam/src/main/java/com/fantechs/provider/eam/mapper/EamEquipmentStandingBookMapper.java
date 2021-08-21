package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentStandingBook;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentStandingBookMapper extends MyMapper<EamEquipmentStandingBook> {
    List<EamEquipmentStandingBookDto> findList(SearchEamEquipmentStandingBook searchEamEquipmentStandingBook);
}