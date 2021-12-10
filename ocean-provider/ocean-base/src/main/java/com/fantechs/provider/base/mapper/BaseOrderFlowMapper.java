package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseOrderFlowMapper extends MyMapper<BaseOrderFlow> {
    List<BaseOrderFlowDto> findList(Map<String, Object> map);

    BaseOrderFlow findOrderFlow(Map<String, Object> map);
}