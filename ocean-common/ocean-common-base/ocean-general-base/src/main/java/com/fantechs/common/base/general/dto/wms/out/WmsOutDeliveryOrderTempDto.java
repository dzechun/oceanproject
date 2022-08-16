package com.fantechs.common.base.general.dto.wms.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutDeliveryOrderTempDto implements Serializable {

    @Transient
    @ApiModelProperty(name="deliveryOrderCode" ,value="出库单编码")
    private String deliveryOrderCode;

    @Transient
    @ApiModelProperty(name="outMaterialTime" ,value="领料时间")
    private String outMaterialTime;

    @Transient
    @ApiModelProperty(name="customerCode" ,value="领料单位(客户)")
    private String customerCode;

    @Transient
    @ApiModelProperty(name="pickMaterialUserName" ,value="领料人")
    private String pickMaterialUserName;

    @Transient
    @ApiModelProperty(name="auditUserName" ,value="审批人")
    private String auditUserName;

    @Transient
    @ApiModelProperty(name="auditTime" ,value="审批时间")
    private String auditTime;

    @Transient
    @ApiModelProperty(name="outMaterialTimeEnd" ,value="领料截止时间")
    private String outMaterialTimeEnd;

    @Transient
    @ApiModelProperty(name="projectId" ,value="项目ID")
    private String projectId;

    @Transient
    @ApiModelProperty(name="confirmCode" ,value="领料单审批状态代码")
    private String confirmCode;

    @Transient
    @ApiModelProperty(name="materialCode" ,value="明细 材料编码")
    private String materialCode;

    @Transient
    @ApiModelProperty(name="remark" ,value="明细 发料备注")
    private String remark;

    @Transient
    @ApiModelProperty(name="option1" ,value="明细 IDGUID")
    private String option1;

    @Transient
    @ApiModelProperty(name="option2" ,value="明细 ISGUID")
    private String option2;

    @Transient
    @ApiModelProperty(name="option3" ,value="明细 PLGUID")
    private String option3;

    @Transient
    @ApiModelProperty(name="option4" ,value="明细 PSGUID")
    private String option4;

    @Transient
    @ApiModelProperty(name="option5" ,value="明细 DHGUID")
    private String option5;

    @Transient
    @ApiModelProperty(name="option6" ,value="明细 专业")
    private String option6;

    @Transient
    @ApiModelProperty(name="option7" ,value="明细 位号")
    private String option7;

    @Transient
    @ApiModelProperty(name="option8" ,value="明细 主项号")
    private String option8;

    @Transient
    @ApiModelProperty(name="option9" ,value="明细 装置号")
    private String option9;

    @Transient
    @ApiModelProperty(name="option10" ,value="明细 申领量")
    private String option10;

    @Transient
    @ApiModelProperty(name="option11" ,value="明细 实发量")
    private String option11;

    @Transient
    @ApiModelProperty(name="dispatchQty" ,value="明细 批准量 发货数量")
    private String dispatchQty;

    @Transient
    @ApiModelProperty(name="pipelineNumber" ,value="明细 管线号")
    private String pipelineNumber;

}
