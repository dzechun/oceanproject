package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionItemMapper extends MyMapper<QmsInspectionItem> {
    List<QmsInspectionItemDto> findList(Map<String, Object> map);

    QmsInspectionItem getMax();
}
