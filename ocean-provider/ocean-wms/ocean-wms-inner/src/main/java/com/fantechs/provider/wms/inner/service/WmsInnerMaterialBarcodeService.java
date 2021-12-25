package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerMaterialBarcodeImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/03.
 */

public interface WmsInnerMaterialBarcodeService extends IService<WmsInnerMaterialBarcode> {

    List<WmsInnerMaterialBarcodeDto> findList(Map<String,Object> map);

    List<WmsInnerMaterialBarcodeDto> add(List<WmsInnerMaterialBarcodeDto> barcodeDtoList,Integer type);

    int batchUpdate(List<WmsInnerMaterialBarcodeDto> list);

    List<WmsInnerMaterialBarcodeDto> batchAdd(List<WmsInnerMaterialBarcodeDto> list);

    LabelRuteDto findLabelRute(Long barcodeRuleSetId,Long materialId);

    int print(String ids,int printQty,String printName,String printType,int printMode);

    Map<String, Object> importExcel(List<WmsInnerMaterialBarcodeImport> importList, List<WmsInnerMaterialBarcodeDto> list,Integer type);

    List<WmsInnerMaterialBarcodeDto> findListByCode(List<String> codes);

}
