package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsCartonDetDto implements Serializable {


    @ApiModelProperty(name="BARCODENO",value = "产品SN")
    private String BARCODENO;

    @ApiModelProperty(name="BOXID",value = "BOXID")
    private String BOXID;

    @ApiModelProperty(name="PN",value = "产品料号")
    private String PN;

    @ApiModelProperty(name="ORDERNO",value = "工单号")
    private String ORDERNO;

    @ApiModelProperty(name="CLIENT",value = "客户端 默认93")
    private String CLIENT;

    @ApiModelProperty(name="CREATEBY",value = "操作人 默认93")
    private String CREATEBY;

    @ApiModelProperty(name="CREATEDATE",value = "操作时间")
    private String CREATEDATE;

}
