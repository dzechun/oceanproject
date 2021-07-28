package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigBarcodeMapper extends MyMapper<EamHtJigBarcode> {
    List<EamHtJigBarcode> findHtList(Map<String,Object> map);
}