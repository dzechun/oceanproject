package com.fantechs.provider.guest.leisai.mapper;

import com.fantechs.common.base.general.dto.leisai.LeisaiHtProductAndHalfOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProductAndHalfOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeisaiHtProductAndHalfOrderMapper extends MyMapper<LeisaiHtProductAndHalfOrder> {
    List<LeisaiHtProductAndHalfOrderDto> findList(Map<String, Object> map);
}