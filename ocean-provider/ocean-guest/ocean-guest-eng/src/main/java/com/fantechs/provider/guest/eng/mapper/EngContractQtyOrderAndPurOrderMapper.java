package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngContractQtyOrderAndPurOrderMapper extends MyMapper<EngContractQtyOrderAndPurOrderDto> {
    List<EngContractQtyOrderAndPurOrderDto> findList(Map<String, Object> map);
}
