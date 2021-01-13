package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDto;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtOtherin;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtOtherinMapper extends MyMapper<WmsInHtOtherin> {
    List<WmsInOtherinDto> findHtList(Map<String, Object> map);
}