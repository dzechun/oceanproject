package com.fantechs.mapper;

import com.fantechs.dto.ProcessRecordUreportDto;

import java.util.List;
import java.util.Map;

public interface ProcessRecordUreportMapper {

    List<ProcessRecordUreportDto> findList(Map<String, Object> map);
}
