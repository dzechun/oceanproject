package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamReturnOrderDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamReturnOrderMapper extends MyMapper<EamReturnOrder> {
    List<EamReturnOrderDto> findList(Map<String,Object> map);
}