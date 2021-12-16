package com.fantechs.provider.wanbao.api.service;

public interface SyncDataService {

    /**
     * 万宝-物料基础信息同步
     * @return
     */
    void syncMaterialData();

    /**
     * 万宝-工单信息同步
     * @return
     */
    void syncOrderData(String workOrderCode);

    /**
     * 万宝-销售订单信息同步
     * @return
     */
    void syncSaleOrderData();

    /**
     * 万宝-出货通知单信息同步
     * @return
     */
    void syncOutDeliveryData();

    /**
     * 万宝-产品条码同步
     * @return
     */
    void syncBarcodeData(boolean flag);
}
