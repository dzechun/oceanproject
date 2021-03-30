package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 出库单编码
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单号")
    private String deliveryOrderCode;

    /**
     * 出货通知单
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单")
    private String shippingNoteCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    private String processorUserName;


    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;

    @ApiModelProperty(name="contractCode" ,value="合同号")
    private String contractCode;

    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @ApiModelProperty(name="productModelName" ,value="产品型号")
    private String productModelName;

    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    @ApiModelProperty(name="modifiedUserId" ,value="操作人")
    private String modifiedUserId;
    /**
     * 开始时间
     */
    @ApiModelProperty(name="modifiedStartTime" ,value="操作日期开始时间(YYYY-MM-DD)")
    private String modifiedStartTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="modifiedEndTime" ,value="操作日期结束时间(YYYY-MM-DD)")
    private String modifiedEndTime;


}
