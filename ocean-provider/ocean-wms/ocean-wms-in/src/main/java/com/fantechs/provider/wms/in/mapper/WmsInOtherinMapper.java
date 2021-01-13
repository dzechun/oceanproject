package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherin;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInOtherinMapper extends MyMapper<WmsInOtherin> {
    List<WmsInOtherinDto> findList(Map<String, Object> map);
}