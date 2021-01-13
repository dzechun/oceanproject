package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInOtherinDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherinDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInOtherinDetMapper extends MyMapper<WmsInOtherinDet> {
    List<WmsInOtherinDetDto> findList(Map<String, Object> map);
}