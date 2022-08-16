package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SapPushMessageApi implements Serializable {


    @ApiModelProperty(name="userName" ,value="推送人名称，对个名称用逗号隔开")
    private String userName;

    @ApiModelProperty(name=" msg" ,value="消息")
    private String  msg;

}
