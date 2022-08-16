package com.fantechs.common.base.entity.security.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSysApiLog extends BaseQuery implements Serializable {

    /**
     * 调用结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="callResult",value = "调用结果(0-失败 1-成功)")
    private Byte callResult;

    /**
     * 接口URL
     */
    @ApiModelProperty(name="apiUrl",value = "接口URL")
    private String apiUrl;

    /**
     * 请求参数
     */
    @ApiModelProperty(name="requestParameter",value = "请求参数")
    private String requestParameter;

    /**
     * 返回数据
     */
    @ApiModelProperty(name="responseData",value = "返回数据")
    private String responseData;

    /**
     * 接口模块
     */
    @ApiModelProperty(name="apiModule",value = "接口模块")
    private String apiModule;
}
