package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigBarcodeMapper extends MyMapper<EamJigBarcode> {
    List<EamJigBarcodeDto> findList(Map<String,Object> map);
}