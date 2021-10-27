package com.fantechs.provider.guest.leisai.mapper;

import com.fantechs.common.base.general.dto.leisai.LeisaiProductAndHalfOrderDto;
import com.fantechs.common.base.general.entity.leisai.LeisaiProductAndHalfOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeisaiProductAndHalfOrderMapper extends MyMapper<LeisaiProductAndHalfOrder> {
    List<LeisaiProductAndHalfOrderDto> findList(Map<String, Object> map);
}