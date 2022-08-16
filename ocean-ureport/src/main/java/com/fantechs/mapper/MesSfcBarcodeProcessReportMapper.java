package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.ureport.*;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcBarcodeProcessReportMapper extends MyMapper<MesSfcBarcodeProcessReport> {
    List<MesSfcBarcodeProcessReport> findList(Map<String, Object> map);

    String findProductBarcodeList(Map<String, Object> map);

 //   List<MesSfcBarcodeProcessRecordDto> findBarcodeList(Map<String, Object> map);

    List<InspectionRecordUreport> findInspectionList(Map<String, Object> map);

    List<BoxRecordUreport> findBoxList(Map<String, Object> map);

    List<PalletRecordUreport> findPalletList(Map<String, Object> map);

    List<ReworkRecordUreport> findReworkList(Map<String, Object> map);

//    List<EquipmentParameterRecordUreport> findEquipmentParameterList(Map<String, Object> map);

    List<AssemblyRecordUreport> findAssemblyList(Map<String, Object> map);
}
