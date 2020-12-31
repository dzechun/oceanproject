package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNote;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmDeliveryNoteMapper extends MyMapper<SrmDeliveryNote> {
    List<SrmDeliveryNoteDto> findList(Map<String, Object> map);

    SrmDeliveryNote getMax();
}

