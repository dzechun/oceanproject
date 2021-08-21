package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamEquipmentBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentBarcodeMapper extends MyMapper<EamEquipmentBarcode> {
    List<EamEquipmentBarcode> findList(Map<String,Object> map);
}