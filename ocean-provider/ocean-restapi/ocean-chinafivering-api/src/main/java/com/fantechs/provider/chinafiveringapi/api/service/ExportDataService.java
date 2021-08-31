package com.fantechs.provider.chinafiveringapi.api.service;

public interface ExportDataService {

    /**
     * 中国五环-入库单回传接口
     * @return
     * 参数：jsonVoiceArray,说明： WMSKey,PPGUID,合同号,请购单号,物流批次号,箱号,材料编码,位号,
     * 主项号,装置号,到货量,货架编号,到货备注,入库校验时间,入库确认时间,登记时间,登记人
     * 参数：projectID,  说明：项目ID = 3919
     * 返回值：成功：success  错误：错误描述（如数据格式不正确）
     */
    String writeDeliveryDetails(String jsonVoiceArray, String projectID) throws Exception;

    /**
     * 中国五环-盘点单回传接口
     * @return
     * 参数：jsonVoiceArray,说明： WMSKey,PPGUID,PSGUID,合同号,请购单号,材料编码,位号,主项号,装置号,
     * 变化量,材料状态,DHGUID,登记时间,登记人
     * 参数：projectID,  说明：项目ID = 3919
     * 返回值：成功：success  错误：错误描述（如数据格式不正确）
     */
    String writeMakeInventoryDetails(String jsonVoiceArray, String projectID) throws Exception;

    /**
     * 中国五环-出库单回传接口
     * @return
     * 参数：jsonVoiceArray,说明： WMSKey,ISGUID,IDGUID,实发量,发料备注,登记时间,登记人
     * 返回值：成功：success  错误：错误描述（如数据格式不正确）
     */
    String writeIssueDetails(String jsonVoiceArray, String ProjectID) throws Exception;

    /**
     * 中国五环-移位调拨单接口
     * @return
     * 参数：jsonVoiceArray,说明： WMSKey,PPGUID,PSGUID,合同号,请购单号,材料编码,位号,主项号,装置号,
     * 变化量,旧DHGUID,新DHGUID,登记时间,登记人
     * 参数：projectID,  说明：项目ID = 3919
     * 返回值：成功：success  错误：错误描述（如数据格式不正确）
     */
    String writeMoveInventoryDetails(String jsonVoiceArray, String projectID) throws Exception;

    /**
     * 中国五环-箱单回传接口
     * @return
     * 参数：jsonVoiceArray,说明： PSGUID,材料编码,位号,设计量,余量,请购量,采购量,备注,主项号,装置号,
     * 材料用途,登记时间,登记人
     * 参数：projectID,  说明：项目ID = 3919
     * 返回值：成功：success  错误：错误描述（如数据格式不正确）
     */
    String writePackingLists(String jsonVoiceArray, String projectID) throws Exception;
}
