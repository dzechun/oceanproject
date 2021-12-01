package com.fantechs.common.base.general.dto.jinan.Import;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RfidAssetImport implements Serializable {

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode" ,value="资产编码")
    @Excel(name = "资产编码(必填)", height = 20, width = 30)
    private String assetCode;

    /**
     * 资产名称
     */
    @ApiModelProperty(name="assetName" ,value="资产名称")
    @Excel(name = "资产名称(必填)", height = 20, width = 30)
    private String assetName;

    /**
     * 资产描述
     */
    @ApiModelProperty(name="assetDesc" ,value="资产描述")
    @Excel(name = "资产描述", height = 20, width = 30)
    private String assetDesc;

    /**
     * RFID
     */
    @ApiModelProperty(name="assetBarcode" ,value="RFID")
    @Excel(name = "RFID", height = 20, width = 30)
    private String assetBarcode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;
}
