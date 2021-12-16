package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchProLineBoard extends BaseQuery implements Serializable {


    /**
     * 线别编码
     */
    @ApiModelProperty(name="proCode" ,value="线别编码")
    private String proCode;

    /**
     * 线别id
     */
    @ApiModelProperty(name="proLineId" ,value="线别id")
    private Long proLineId;

    /**
     * 设备状态
     */
    @ApiModelProperty(name="equipmentStatus" ,value="设备状态")
    private List<Byte> equipmentStatus;

    /**
     * 过站状态
     */
    @ApiModelProperty(name="barcodeStatus" ,value="过站状态")
    private Byte barcodeStatus;

    /**
     * 过站次数
     */
    @ApiModelProperty(name="passStationCount" ,value="过站次数")
    private Byte passStationCount;


    /**
     * 工段名称
     */
    @ApiModelProperty(name="sectionName" ,value="工段名称")
    private String sectionName;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode" ,value="工序编码")
    private String processCode;

    /**
     * 是否需要去重
     */
    @ApiModelProperty(name="processCode" ,value="是否需要去重")
    private byte isDistinct;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单编码")
    private String workOrderCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private String workOrderId;

    /**
     * 序号
     */
    @ApiModelProperty(name="seqNum" ,value="序号")
    private int seqNum;




}
