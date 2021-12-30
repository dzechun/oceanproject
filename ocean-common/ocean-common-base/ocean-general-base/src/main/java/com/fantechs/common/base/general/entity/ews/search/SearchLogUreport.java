package com.fantechs.common.base.general.entity.ews.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/29
 */
@Data
public class SearchLogUreport extends BaseQuery implements Serializable {
    /**
     * 预警事件ID
     */
    @ApiModelProperty(name = "warningEventIdCode",value = "预警事件ID")
    private String warningEventIdCode;

    /**
     *预警事件名称
     */
    @ApiModelProperty(name = "warningEventName",value = "预警事件名称")
    private String warningEventName;

    /**
     *执行方式（1-Get 2-Post）
     */
    @ApiModelProperty(name = "callingMethod",value = "执行方式(1-GET 2-POST)")
    private Byte callingMethod;

    /**
     *通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name = "notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    private Byte notificationMethod;

    /**
     * 执行结果(1-成功 2-失败)
     */
    @ApiModelProperty(name = "executeResult",value = "执行结果(1-成功 2-失败)")
    private Byte executeResult;

    /**
     * 推送结果(1-未推送 2-已推送)
     */
    @ApiModelProperty(name = "messagePushResult",value = "推送结果(1-未推送 2-已推送)")
    private Byte messagePushResult;

    /**
     * 处理结果(1-未确认 2-已确认 3-不予处理)
     */
    @ApiModelProperty(name = "handleResult",value = "处理结果(1-未确认 2-已确认 3-不予处理)")
    private Byte handleResult;

    /**
     * 人员等级（1、一级人员 2、二级人员 3、三级人员 4、四级人员）
     */
    @ApiModelProperty(name = "personnelLevel",value = "人员等级（1、一级人员 2、二级人员 3、三级人员 4、四级人员）")
    private Byte personnelLevel;
}
