package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.IService;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 补打查询列表
     * @param searchMesSfcWorkOrderBarcode
     * @return
     */
    ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> findListByReprint(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    int print(String ids,Byte printType,String printName,String userCode,String password,String printId);
    int print_rewrite(String ids,Byte printType,String printName,String userCode,String password,String printId);

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
    List<String> wanbaoAddCustomerBarcode(Long salesOrderDetId, String fixedValue, String initialValue, String finalValue, boolean isImport);

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

    int printByOrderCode(Long id,Byte barcodeType,String printName,String userCode,String password,String printId);

    /**
     * 同步PQMS系统，批量处理条码数据
     * @param dto
     * @return
     */
    int batchSyncBarcode(BatchSyncBarcodeDto dto);

    /**
     * 同步PQMS系统，查找条码以及条码流程表
     * @param labelCategoryId
     * @return
     */
    SyncFindBarcodeDto syncFindBarcode(Long labelCategoryId, List<String> barcodeList);


    /**
     * 导入客户条码
     * @param list
     */
    Map<String, Object> importExcel(List<ImportCustomerBarcodeDto> list);

    Long finByTypeId(@Param("labelCategoryName")String labelCategoryName);

}
