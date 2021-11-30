package com.fantechs.common.base.general.dto.jinan.Import;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RfidBaseStationImport implements Serializable {

    /**
     * 基站编码
     */
    @ApiModelProperty(name="baseStationCode" ,value="基站编码")
    @Excel(name = "基站编码(必填)", height = 20, width = 30)
    private String baseStationCode;

    /**
     * 基站名称
     */
    @ApiModelProperty(name="baseStationName" ,value="基站名称")
    @Excel(name = "基站名称(必填)", height = 20, width = 30)
    private String baseStationName;

    /**
     * 基站描述
     */
    @ApiModelProperty(name="baseStationDesc" ,value="基站描述")
    @Excel(name = "基站描述", height = 20, width = 30)
    private String baseStationDesc;

    /**
     * 基站型号
     */
    @ApiModelProperty(name="baseStationModel" ,value="基站型号")
    @Excel(name = "基站型号", height = 20, width = 30)
    private String baseStationModel;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode",value = "区域编码")
    @Excel(name = "区域编码", height = 20, width = 30)
    private String areaCode;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="areaId",value = "区域ID")
    private Long areaId;

    /**
     * 基站IP
     */
    @ApiModelProperty(name="baseStationIp",value = "基站IP")
    @Excel(name = "基站IP", height = 20, width = 30)
    private String baseStationIp;

    /**
     * 基站MAC(必填)
     */
    @ApiModelProperty(name="baseStationMac",value = "基站MAC(必填)")
    @Excel(name = "基站MAC(必填)", height = 20, width = 30)
    private String baseStationMac;

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30)
    private String assetCode;

    /**
     * 资产ID
     */
    @ApiModelProperty(name="assetId",value = "资产ID")
    private Long assetId;
}
