package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.WanbaoErpLogicDto;
import com.fantechs.common.base.general.entity.basic.WanbaoErpLogic;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoErpLogicMapper extends MyMapper<WanbaoErpLogic> {
    List<WanbaoErpLogicDto> findList(Map<String,Object> map);
}