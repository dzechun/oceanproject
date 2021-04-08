package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.LabelRuteDto;
import com.fantechs.common.base.general.dto.bcm.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.bcm.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.bcm.search.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.support.IService;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/07.
 */

public interface MesSfcWorkOrderBarcodeService extends IService<MesSfcWorkOrderBarcode> {
    List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    int print(String ids);

    LabelRuteDto findLabelRute(Long workOrderId,Byte barcodeType);

    void checkOutLabel(String labelName, HttpServletRequest request, HttpServletResponse response);

    MesSfcWorkOrderBarcode add(MesSfcWorkOrderBarcode record);
}
