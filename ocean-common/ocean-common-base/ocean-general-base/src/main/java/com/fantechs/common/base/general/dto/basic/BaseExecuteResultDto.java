package com.fantechs.common.base.general.dto.basic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
/*
* 接口执行返回结果
*
*/
@Data
public class BaseExecuteResultDto implements Serializable {

    /**
     * 执行结果 true or false
     */
    @Transient
    @ApiModelProperty(name = "isSuccess",value = "是否成功")
    private Boolean isSuccess;

    /**
     * 成功返回消息
     */
    @Transient
    @ApiModelProperty(name = "successMsg",value = "成功返回消息")
    private String successMsg;

    /**
     * 失败返回消息
     */
    @Transient
    @ApiModelProperty(name = "failMsg",value = "失败返回消息")
    private String failMsg;

    /**
     * 返回结果集
     */
    @Transient
    @ApiModelProperty(name="executeResult",value = "返回结果集")
    private Object executeResult;
}
