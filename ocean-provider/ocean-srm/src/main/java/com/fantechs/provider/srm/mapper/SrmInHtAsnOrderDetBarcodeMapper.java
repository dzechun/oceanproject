package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDetBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInHtAsnOrderDetBarcodeMapper extends MyMapper<SrmInHtAsnOrderDetBarcode> {
    List<SrmInHtAsnOrderDetBarcode> findList(Map<String, Object> map);
}