package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcWorkOrderPartBarcodeMapper extends MyMapper<MesSfcWorkOrderPartBarcode> {
    List<MesSfcWorkOrderPartBarcodeDto> findList(Map<String, Object> map);
}