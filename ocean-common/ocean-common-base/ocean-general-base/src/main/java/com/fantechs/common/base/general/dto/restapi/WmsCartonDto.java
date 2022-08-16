package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class WmsCartonDto implements Serializable {

    @Id
    @ApiModelProperty(name="BOXID",value = "BOXID")
    private String BOXID;

    @ApiModelProperty(name="STATUS",value = "状态 默认1")
    private String STATUS;

    @ApiModelProperty(name="CREATEBY",value = "创建人 默认mes")
    private String CREATEBY;

    @ApiModelProperty(name="CREATEDATE",value = "创建时间")
    private String CREATEDATE;

    @ApiModelProperty(name="CLIENT",value = "客户端 默认93")
    private String CLIENT;

    @ApiModelProperty(name="PRINTCOUNT",value = "打印次数 默认1")
    private String PRINTCOUNT;

    @ApiModelProperty(name="PRINTBY",value = "打印人 默认mes")
    private String PRINTBY;

    @ApiModelProperty(name="PRINTDATE",value = "打印时间")
    private String PRINTDATE;

    @ApiModelProperty(name = "wmsCartonDetDtoList", value = "包箱明细")
    @Transient
    private List<WmsCartonDetDto> wmsCartonDetDtoList;

}
