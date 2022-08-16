package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeReprintDto;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcWorkOrderBarcodeReprintMapper extends MyMapper<MesSfcWorkOrderBarcodeReprint> {
    List<MesSfcWorkOrderBarcodeReprintDto> findList(Map<String, Object> map);
}