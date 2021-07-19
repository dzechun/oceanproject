package com.fantechs.common.base.general.entity.ureport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReworkRecordUreport extends ValidGroup implements Serializable {

    /**
     * 返工单号
     */
    @ApiModelProperty(name="reworkOrderCode",value = "返工单号")
    private String reworkOrderCode;

    /**
     * 返工工艺路线
     */
    @ApiModelProperty(name="routeName",value = "返工工艺路线")
    private String routeName;

    /**
     * 返工工序
     */
    @ApiModelProperty(name="processName",value = "返工工序")
    private String processName;

    /**
     * 返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)
     */
    @ApiModelProperty(name="reworkStatus",value = "返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)")
    private Byte reworkStatus;


}
