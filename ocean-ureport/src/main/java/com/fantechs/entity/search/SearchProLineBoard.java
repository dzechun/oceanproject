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
     * 设备状态
     */
    @ApiModelProperty(name="sectionName" ,value="设备状态")
    private String sectionName;
}
