package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItemDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsInspectionItemDetMapper extends MyMapper<QmsInspectionItemDet> {
    List<QmsInspectionItemDetDto> findList(Map<String, Object> map);
}
