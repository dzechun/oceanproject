package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmsInAsnOrderDetMapper extends MyMapper<WmsInAsnOrderDet> {
    List<WmsInAsnOrderDetDto> findList(SearchWmsInAsnOrderDet searchWmsInAsnOrderDet);

    Integer findLineNumber(@Param("asnOrderId")Long asnOrderId);
}