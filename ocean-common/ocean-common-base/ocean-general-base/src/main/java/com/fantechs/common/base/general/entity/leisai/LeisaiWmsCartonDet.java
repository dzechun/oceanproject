package com.fantechs.common.base.general.entity.leisai;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class LeisaiWmsCartonDet implements Serializable {

    @Id
    @ApiModelProperty(name="BARCODENO",value = "产品SN")
    @Column(name = "BARCODENO")
    private String BARCODENO;

    @ApiModelProperty(name="BOXID",value = "BOXID")
    @Column(name = "BOXID")
    private String BOXID;

    @ApiModelProperty(name="PN",value = "产品料号")
    @Column(name = "PN")
    private String PN;

    @ApiModelProperty(name="ORDERNO",value = "工单号")
    @Column(name = "ORDERNO")
    private String ORDERNO;

    @ApiModelProperty(name="CLIENT",value = "客户端 默认93")
    @Column(name = "CLIENT")
    private String CLIENT;

    @ApiModelProperty(name="CREATEBY",value = "操作人 默认93")
    @Column(name = "CREATEBY")
    private String CREATEBY;

    @ApiModelProperty(name="CREATEDATE",value = "操作时间")
    @Column(name = "CREATEDATE")
    private String CREATEDATE;

}
