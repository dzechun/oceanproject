package com.fantechs.common.base.general.entity.ureport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InspectionRecordUreport extends ValidGroup implements Serializable {

    /**
     * IPOC单号
     */
    @ApiModelProperty(name="ipqcInspectionOrderCode",value = "IPOC单号")
    private String ipqcInspectionOrderCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    private String qty;

    /**
     * 检验方式
     */
    @ApiModelProperty(name="inspectionWayDesc",value = "检验方式")
    private String inspectionWayDesc;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验标准")
    private String inspectionStandardName;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    private Byte inspectionStatus;

    /**
     * 检验结果(1-合格 2-不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(1-合格 2-不合格)")
    private Byte inspectionResult;

    /**
     * 审批状态(1-待审批 2-已审批)
     */
    @ApiModelProperty(name="auditStatus",value = "审批状态(1-待审批 2-已审批)")
    private Byte auditStatus;

    /**
     * 审批人
     */
    @ApiModelProperty(name="auditName",value = "产品型号")
    private String auditName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    private Date createTime;

}
