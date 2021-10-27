package com.fantechs.provider.guest.leisai.mapper;

import com.fantechs.common.base.general.dto.leisai.LeisaiHtProcessInputOrderDto;
import com.fantechs.common.base.general.entity.leisai.history.LeisaiHtProcessInputOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeisaiHtProcessInputOrderMapper extends MyMapper<LeisaiHtProcessInputOrder> {
    List<LeisaiHtProcessInputOrderDto> findList(Map<String, Object> map);
}