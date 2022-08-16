package com.fantechs.service;

import com.fantechs.dto.ProcessRecordUreportDto;

import java.util.List;
import java.util.Map;

public interface ProcessRecordUreportService {

    List<ProcessRecordUreportDto> findList(Map<String, Object> map);
}
