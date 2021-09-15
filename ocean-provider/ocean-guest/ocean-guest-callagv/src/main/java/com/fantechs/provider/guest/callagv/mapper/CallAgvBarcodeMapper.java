package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvBarcodeMapper extends MyMapper<CallAgvBarcode> {
    List<CallAgvBarcodeDto> findList(Map<String, Object> map);
}