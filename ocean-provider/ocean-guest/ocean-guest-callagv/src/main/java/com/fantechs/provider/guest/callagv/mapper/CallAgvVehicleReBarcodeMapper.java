package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.dto.callagv.CallAgvStorageMaterialDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvStorageMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvVehicleReBarcodeMapper extends MyMapper<CallAgvVehicleReBarcode> {
    List<CallAgvVehicleReBarcodeDto> findList(Map<String, Object> map);

    List<CallAgvStorageMaterialDto> callAgvStorageMaterialList(SearchCallAgvStorageMaterial searchCallAgvStorageMaterial);
}