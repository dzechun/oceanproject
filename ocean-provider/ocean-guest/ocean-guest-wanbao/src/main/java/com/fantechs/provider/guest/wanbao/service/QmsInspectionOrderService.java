package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */

public interface QmsInspectionOrderService extends IService<QmsInspectionOrder> {
    List<QmsInspectionOrder> findList(Map<String, Object> map);
    int writeBack(Long inspectionOrderId);
    int audit(QmsInspectionOrder qmsInspectionOrder);
    //    int autoAdd();
    int newAutoAdd();

    /**
     * 成品检验生成质检移位单
     * @param qmsInspectionOrder
     * @param type (0-正常更新 ，1-返写检验单据)
     * @return
     */
    int update(QmsInspectionOrder qmsInspectionOrder,Byte type);
    int thirdInspection(QmsInspectionOrder qmsInspectionOrder);
    int batchQualified(Long inspectionOrderId);
    int recheckBatchQualified(Long inspectionOrderId);
    int batchSubmit(Long inspectionOrderId);
    QmsInspectionOrder selectByKey(Long key);
    int recheck(Long inspectionOrderId);
    int updateSampleQty(Long inspectionOrderId, BigDecimal sampleQty);

    /**
     * 成品检验生成质检移位单
     * @param ids
     * @return
     */
    int qmsInspectToInnerJobShift(String ids);

    int exemption(QmsInspectionOrder qmsInspectionOrder);

    /**
     * 条码走产线，自动复检
     * @param barcode
     * @return
     */
    int recheckByBarcode(String barcode);
}
