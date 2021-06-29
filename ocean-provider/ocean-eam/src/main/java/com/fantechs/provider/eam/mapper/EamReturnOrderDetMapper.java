package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamReturnOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamReturnOrderDetMapper extends MyMapper<EamReturnOrderDet> {
    List<EamReturnOrderDetDto> findList(Map<String,Object> map);
}