package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtEquipmentStandingBookDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentStandingBookMapper extends MyMapper<EamHtEquipmentStandingBook> {
    List<EamHtEquipmentStandingBookDto> findHtList(Map<String,Object> map);
}