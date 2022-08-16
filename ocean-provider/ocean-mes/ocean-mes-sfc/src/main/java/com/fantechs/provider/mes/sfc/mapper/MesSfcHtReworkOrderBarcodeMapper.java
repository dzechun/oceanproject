package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcHtReworkOrderBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcHtReworkOrderBarcodeMapper extends MyMapper<MesSfcHtReworkOrderBarcode> {

    List<MesSfcHtReworkOrderBarcodeDto> findList(Map<String, Object> map);
}