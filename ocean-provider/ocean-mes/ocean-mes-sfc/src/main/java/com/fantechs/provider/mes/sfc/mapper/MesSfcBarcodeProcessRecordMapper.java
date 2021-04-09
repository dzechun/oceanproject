package com.fantechs.provider.mes.sfc.mapper;


import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcBarcodeProcessRecordMapper extends MyMapper<MesSfcBarcodeProcessRecord> {
    List<MesSfcBarcodeProcessRecordDto> findList(Map<String, Object> map);
}