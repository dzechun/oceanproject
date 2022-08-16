package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "IPOC单号", height = 20, width = 30,orderNum = "1")
    private String ipqcInspectionOrderCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum = "2")
    private String processName;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum = "3")
    private String qty;

    /**
     * 检验方式
     */
    @ApiModelProperty(name="inspectionWayDesc",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum = "4")
    private String inspectionWayDesc;

    /**
     * 检验标准
     */
    @ApiModelProperty(name="inspectionStandardName",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum = "5")
    private String inspectionStandardName;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态", height = 20, width = 30,orderNum = "6",replace = {"待检验_1","检验中_2","已检验_3"})
    private Byte inspectionStatus;

    /**
     * 检验结果(1-合格 2-不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(1-合格 2-不合格)")
    @Excel(name = "检验结果", height = 20, width = 30,orderNum = "7",replace = {"合格_1","不合格_2"})
    private Byte inspectionResult;

    /**
     * 审批状态(1-待审批 2-已审批)
     */
    @ApiModelProperty(name="auditStatus",value = "审批状态(1-待审批 2-已审批)")
    @Excel(name = "审批状态", height = 20, width = 30,orderNum = "8",replace = {"待审批_1","已审批_2"})
    private Byte auditStatus;

    /**
     * 审批人
     */
    @ApiModelProperty(name="auditName",value = "产品型号")
    @Excel(name = "箱码", height = 20, width = 30,orderNum = "9")
    private String auditName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "箱码", height = 20, width = 30,orderNum = "10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
