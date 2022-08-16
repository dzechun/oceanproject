package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigRequisition extends BaseQuery implements Serializable {

    /**
     * 领用状态（0-已归还 1-已领用）
     */
    @ApiModelProperty(name="requisitionStatus",value = "领用状态（0-已归还 1-已领用）")
    private Byte requisitionStatus;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 治具条码id
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码id")
    private Long jigBarcodeId;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    private String jigBarcode;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    private Long jigId;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    private String jigName;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    private String jigModel;

    /**
     * 治具类别
     */
    @ApiModelProperty(name="jigCategoryName",value = "治具类别")
    private String jigCategoryName;

    /**
     * 领用日期
     */
    @ApiModelProperty(name="requisitionTime",value = "领用日期")
    private String requisitionTime;

    /**
     * 归还日期
     */
    @ApiModelProperty(name="returnTime",value = "归还日期")
    private String returnTime;

    /**
     * 是否除去已归还的治具的领用记录（0-否 1-是）
     */
    @ApiModelProperty(name="ifExceptReturn",value = "是否除去已归还的治具的领用记录（0-否 1-是）")
    private Integer ifExceptReturn;
}
