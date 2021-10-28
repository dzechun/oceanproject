package com.fantechs.provider.guest.leisai.mapper;

import com.fantechs.common.base.general.dto.leisai.LeisaiProcessInputOrderDto;
import com.fantechs.common.base.general.entity.leisai.LeisaiProcessInputOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeisaiProcessInputOrderMapper extends MyMapper<LeisaiProcessInputOrder> {
    List<LeisaiProcessInputOrderDto> findList(Map<String, Object> map);
}