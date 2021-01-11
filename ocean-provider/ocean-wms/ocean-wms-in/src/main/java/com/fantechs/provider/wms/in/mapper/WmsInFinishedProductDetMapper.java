package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProductDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInFinishedProductDetMapper extends MyMapper<WmsInFinishedProductDet> {
    List<WmsInFinishedProductDetDto> findList(Map<String, Object> map);
}