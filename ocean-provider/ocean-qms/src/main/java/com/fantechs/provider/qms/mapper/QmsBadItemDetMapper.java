package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsBadItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItemDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsBadItemDetMapper extends MyMapper<QmsBadItemDet> {
    List<QmsBadItemDetDto> findList(Map<String, Object> map);

    List<QmsBadItemDetDto> findById(Long badItemId);
}
