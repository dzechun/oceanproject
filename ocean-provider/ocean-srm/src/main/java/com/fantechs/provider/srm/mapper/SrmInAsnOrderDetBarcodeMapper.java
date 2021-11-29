package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDetBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInAsnOrderDetBarcodeMapper extends MyMapper<SrmInAsnOrderDetBarcode> {
    List<SrmInAsnOrderDetBarcodeDto> findList(Map<String, Object> map);
}