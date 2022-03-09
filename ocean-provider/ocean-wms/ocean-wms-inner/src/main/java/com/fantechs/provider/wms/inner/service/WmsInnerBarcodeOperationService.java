package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerBarcodeOperationDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerBarcodeOperation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2022/03/09.
 */

public interface WmsInnerBarcodeOperationService extends IService<WmsInnerBarcodeOperation> {
    List<WmsInnerBarcodeOperationDto> findList(Map<String, Object> map);

    /**
     * 扫条码返回数据
     * @param barcode
     * @return
     */
    WmsInnerInventoryDetDto scanBarcode(String barcode);

    /**
     * 扫替换条码返回数据
     * @param barcode
     * @return
     */
    WmsInnerInventoryDetDto scanReplaceBarcode(String barcode,String replaceBarcode);

    /**
     * 替换操作提交
     * @param barcode
     * @return
     */
    int replaceCommit(String barcode,String replaceBarcode);

    /**
     * 报废操作扫描条码
     * @param barcode
     * @return
     */
    WmsInnerInventoryDetDto scanCrapBarcode(String barcode);

    /**
     * 报废操作提交
     * @param barcode
     * @return
     */
    int crapCommit(String barcode);

    //Map<String, Object> importExcel(List<WmsInnerBarcodeOperation> list);
}
