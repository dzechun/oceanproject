package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutShippingNoteDetMapper extends MyMapper<WmsOutShippingNoteDet> {
    List<WmsOutShippingNoteDetDto> findList(Map<String, Object> map);
}