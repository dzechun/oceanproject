package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamSparePartDto;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamSparePartMapper extends MyMapper<EamSparePart> {
    List<EamSparePartDto> findList(Map<String,Object> map);
}