package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDetDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmDeliveryNoteDetMapper extends MyMapper<SrmDeliveryNoteDet> {
    List<SrmDeliveryNoteDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<SrmDeliveryNoteDet> list);
}
