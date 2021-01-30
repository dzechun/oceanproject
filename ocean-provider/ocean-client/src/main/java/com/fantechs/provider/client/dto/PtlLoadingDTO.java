package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PtlLoadingDTO {

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    private String loadingCode;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 用户账号
     */
    @ApiModelProperty(name = "user",value = "用户账号")
    private String user;

    /**
     * 上料单明细
     */
    @ApiModelProperty(name="smtLoadingDetDtoList",value = "上料单明细")
    private List<PtlLoadingDetDTO> ptlLoadingDetDTOList;
}
