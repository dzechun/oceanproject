package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.restapi.WmsDataExportInnerJobOrderDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsDataExportInnerJobOrderMapper extends MyMapper<WmsDataExportInnerJobOrderDto> {
    List<WmsDataExportInnerJobOrderDto> findExportData(Map<String, Object> map);
}
