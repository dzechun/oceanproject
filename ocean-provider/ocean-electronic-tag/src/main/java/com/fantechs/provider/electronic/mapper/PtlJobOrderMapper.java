package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlJobOrderMapper extends MyMapper<PtlJobOrder> {
    List<PtlJobOrderDto> findList(Map<String, Object> map);
}