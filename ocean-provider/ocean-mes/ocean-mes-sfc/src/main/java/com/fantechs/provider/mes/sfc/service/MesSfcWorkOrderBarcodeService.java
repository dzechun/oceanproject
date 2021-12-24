package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
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

    int print(String ids,Byte printType,String printName,String userCode,String password);

    LabelRuteDto findLabelRute(Long workOrderId, Byte barcodeType);

    void checkOutLabel(String labelName, HttpServletRequest request, HttpServletResponse response);

    List<MesSfcWorkOrderBarcode> add(MesSfcWorkOrderBarcode record);

    MesSfcWorkOrderBarcode findBarcode(String barcode);

    /**
     * 批量修改生产订单条码状态
     * @param workOrderBarcodes
     * @return
     */
    int batchUpdate(List<MesSfcWorkOrderBarcode> workOrderBarcodes);

    List<MesSfcWorkOrderBarcodeDto> findByWorkOrderGroup(Map<String, Object> map);

    List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map);

    List<MesSfcWorkOrderBarcodeDto> findListByPalletDet(Map<String, Object> map);

    List<MesSfcWorkOrderBarcodeDto> findListByCartonDet(Map<String, Object> map);

}
