package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcodeReprint;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcWorkOrderPartBarcodeReprintMapper extends MyMapper<MesSfcWorkOrderPartBarcodeReprint> {
    List<MesSfcWorkOrderPartBarcodeReprintDto> findList(Map<String, Object> map);
}