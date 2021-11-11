package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchEsopWorkInstruction extends BaseQuery implements Serializable {

    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    private Long workInstructionId;

    /**
     * WI编码
     */
    @ApiModelProperty(name="workInstructionCode",value = "WI编码")
    private String workInstructionCode;

    /**
     * WI名称
     */
    @ApiModelProperty(name="workInstructionName",value = "WI名称")
    private String workInstructionName;


    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Transient
    private String materialCode;

    /**
     * 产品物料ID
     */
    @ApiModelProperty(name="materialId",value = "产品物料ID")
    @Transient
    private String materialId;

    /**
     * 所属工序ID
     */
    @ApiModelProperty(name="processId",value = "所属工序ID")
    private Long processId;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    private String equipmentIp;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId",value = "线别ID")
    private Long proLineId;

    /**
     * MAC地址
     */
    @ApiModelProperty(name="equipmentMacAddress",value = "MAC地址")
    private String equipmentMacAddress;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    private String productModelCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;


    /**
     * 设备序号
     */
    @ApiModelProperty(name="equipmentSeqNum",value = "设备序号")
    private String equipmentSeqNum;

    /**
     * WI序号(文件编号)
     */
    @ApiModelProperty(name="workInstructionSeqNum",value = "WI序号(文件编号)")
    private String workInstructionSeqNum;

}
