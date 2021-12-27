package com.fantechs.common.base.general.entity.ews.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/27
 */
@Data
public class SearchEwsWarningUserInfo extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "userCode",value = "用户工号")
    private String userCode;

    @ApiModelProperty(name = "nickName",value = "用户名称")
    private String nickName;
}
