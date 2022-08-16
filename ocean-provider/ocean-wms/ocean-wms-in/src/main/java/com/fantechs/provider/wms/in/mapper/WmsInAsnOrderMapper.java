package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInAsnOrderMapper extends MyMapper<WmsInAsnOrder> {
    List<WmsInAsnOrderDto> findList(SearchWmsInAsnOrder searchWmsInAsnOrder);

    String findBarCode(@Param("id")Long id);

    Integer findLineNumber(@Param("asnOrderId")Long asnOrderId);

    Long findDefaultStatus(Map<String ,Object> map);
}