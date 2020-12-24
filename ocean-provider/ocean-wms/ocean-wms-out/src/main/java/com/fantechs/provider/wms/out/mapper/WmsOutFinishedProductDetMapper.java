package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProductDet;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutFinishedProductDetMapper extends MyMapper<WmsOutFinishedProductDet> {
    List<WmsOutFinishedProductDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}