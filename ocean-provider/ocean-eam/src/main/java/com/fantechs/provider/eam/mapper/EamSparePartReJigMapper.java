package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamSparePartReJigDto;
import com.fantechs.common.base.general.entity.eam.EamSparePartReJig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamSparePartReJigMapper extends MyMapper<EamSparePartReJig> {
    List<EamSparePartReJigDto> findList(Map<String,Object> map);
}