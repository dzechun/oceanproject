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

    /**
     * 万宝项目新增客户条码
     * @param salesOrderDetId
     * @param fixedValue
     * @param initialValue
     * @param finalValue
     * @return
     */
    List<String> wanbaoAddCustomerBarcode(Long salesOrderDetId, String fixedValue, String initialValue, String finalValue);

    /**
     * 万宝项目删除客户条码
     * @param salesOrderDetId
     * @param fixedValue
     * @return
     */
    int wanbaoDeleteCustomerBarcode(Long salesOrderDetId, String fixedValue);

    /**
     * 万宝项目按销售订单明细查询客户条码
     * @param salesOrderDetId
     * @return
     */
    List<MesSfcWorkOrderBarcode> wanbaoFindCustomerBarcode(Long salesOrderDetId);

    int printByOrderCode(Long id,Byte barcodeType,String printName,String userCode,String password);

}
