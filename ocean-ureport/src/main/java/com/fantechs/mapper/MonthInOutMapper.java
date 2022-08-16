package com.fantechs.mapper;

import com.fantechs.dto.MonthInDto;
import com.fantechs.dto.MonthOutDto;
import com.fantechs.dto.ShipmentDetDto;
import com.fantechs.entity.MonthInOutModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */

@Mapper
public interface MonthInOutMapper {
    List<MonthInDto> findInList(Map<String,Object> map);

    List<String> findInListBarCode(Map<String ,Object> map);

    List<MonthOutDto> findOutList(Map<String,Object> map);

    List<String> findOutListBarCode(Map<String ,Object> map);

    List<ShipmentDetDto> findShipmentDet(Map<String ,Object> map);
}
