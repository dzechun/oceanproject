package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNote;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutShippingNoteMapper extends MyMapper<WmsOutShippingNote> {
    List<WmsOutShippingNoteDto> findList(Map<String, Object> map);
}