package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcReworkOrderBarcodeMapper extends MyMapper<MesSfcReworkOrderBarcode> {
    List<MesSfcReworkOrderBarcodeDto> findList(Map<String, Object> map);
}