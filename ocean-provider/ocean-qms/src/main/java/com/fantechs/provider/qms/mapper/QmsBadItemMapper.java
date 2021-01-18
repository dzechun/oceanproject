package com.fantechs.provider.qms.mapper;


import com.fantechs.common.base.general.dto.qms.QmsBadItemDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItem;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsBadItemMapper extends MyMapper<QmsBadItem> {
    List<QmsBadItemDto> findList(Map<String, Object> map);
}
