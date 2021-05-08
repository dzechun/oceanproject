package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerJobOrderMapper extends MyMapper<WmsInnerJobOrder> {
    List<WmsInnerJobOrderDto> findList(Map<String,Object> map);

    Long findStorage(@Param("materialId")Long materialId);

    Long SelectStorage();
}