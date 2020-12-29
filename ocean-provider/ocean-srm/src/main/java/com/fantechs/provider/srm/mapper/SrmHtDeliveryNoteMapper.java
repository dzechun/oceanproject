package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryNote;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtDeliveryNoteMapper extends MyMapper<SrmHtDeliveryNote> {
    List<SrmHtDeliveryNote> findList(Map<String, Object> map);
}
