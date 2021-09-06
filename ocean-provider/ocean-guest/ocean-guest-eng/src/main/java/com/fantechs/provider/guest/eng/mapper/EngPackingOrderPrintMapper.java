package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@Mapper
public interface EngPackingOrderPrintMapper {
    List<EngPackingOrderPrintDto> findList(Map<String ,Object> map);

    PrintModel ViewPrint(Map<String,Object> map);
}
