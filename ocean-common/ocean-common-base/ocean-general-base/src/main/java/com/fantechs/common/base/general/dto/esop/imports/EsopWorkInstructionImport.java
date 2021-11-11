package com.fantechs.common.base.general.dto.esop.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class EsopWorkInstructionImport implements Serializable {


    /**
     * WI编码
     */
    @ApiModelProperty(name="workInstructionCode",value = "WI编码")
    @Excel(name = "WI编码", height = 20, width = 30)
    private String workInstructionCode;

    /**
     * WI名称
     */
    @ApiModelProperty(name="workInstructionName",value = "WI名称")
    @Excel(name = "WI名称", height = 20, width = 30,orderNum="2")
    private String workInstructionName;

    /**
     * WI版本
     */
    @ApiModelProperty(name="workInstructionVer",value = "WI版本")
    @Excel(name = "WI版本", height = 20, width = 30)
    private String workInstructionVer;

    /**
     * 文件编号
     */
    @ApiModelProperty(name="workInstructionSeqNum",value = "文件编号")
    @Excel(name = "文件编号", height = 20, width = 30)
    private String workInstructionSeqNum;


    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30)
    private String processCode;


    /**
     *  产品型号
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelCode;

    /**
     * 标准工时
     */
    @ApiModelProperty(name="standardTime",value = "标准工时")
    @Excel(name = "标准工时", height = 20, width = 30)
    private BigDecimal standardTime;


    /**
     * 编制
     */
    @ApiModelProperty(name="writeMakeUserName",value = "编制")
    @Excel(name = "编制", height = 20, width = 30)
    private String writeMakeUserName;


    /**
     * 审核
     */
    @ApiModelProperty(name="auditUserName",value = "审核")
    @Excel(name = "审核", height = 20, width = 30)
    private String auditUserName;

    /**
     * 审批
     */
    @ApiModelProperty(name="approveUserName",value = "审批")
    @Excel(name = "审批", height = 20, width = 30)
    private String approveUserName;


    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;



    /**
     * 制程
     */
    @ApiModelProperty(name="manufactureProcedure",value = "制程")
    @Excel(name = "制程", height = 20, width = 30)
    private String manufactureProcedure;

    /**
     * 工艺要求及注意事项
     */
    @ApiModelProperty(name="processReqAndAnnouncements",value = "工艺要求及注意事项")
    @Excel(name = "工艺要求及注意事项", height = 20, width = 30)
    private String processReqAndAnnouncements;

    /**
     * 是否直接显示视频(0-否 1-是)
     */
    @ApiModelProperty(name="ifShowVideo",value = "是否直接显示视频(0-否 1-是)")
    @Excel(name = "是否直接显示视频", height = 20, width = 30)
    private Byte ifShowVideo;

}
