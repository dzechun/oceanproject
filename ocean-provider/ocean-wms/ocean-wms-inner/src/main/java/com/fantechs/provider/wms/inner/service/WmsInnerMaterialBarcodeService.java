package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
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

    List<WmsInnerMaterialBarcodeDto> findList(SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode);

    List<WmsInnerMaterialBarcodeDto> add(WmsInnerMaterialBarcodeDto wmsInnerMaterialBarcodeDto);

    BaseBarcodeRuleDto findLabelRute(Long barcodeRuleSetId);

    int print(String ids,int printNum);
}
