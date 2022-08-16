package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "返工单号", height = 20, width = 30,orderNum = "1")
    private String reworkOrderCode;

    /**
     * 返工工艺路线
     */
    @ApiModelProperty(name="routeName",value = "返工工艺路线")
    @Excel(name = "返工工艺路线", height = 20, width = 30,orderNum = "2")
    private String routeName;

    /**
     * 返工工序
     */
    @ApiModelProperty(name="processName",value = "返工工序")
    @Excel(name = "返工工序", height = 20, width = 30,orderNum = "3")
    private String processName;

    /**
     * 返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)
     */
    @ApiModelProperty(name="reworkStatus",value = "返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)")
    @Excel(name = "返工状态", height = 20, width = 30,orderNum = "4",replace = {"未返工_1","返工中_2","返工完成_3","已撤消_4"})
    private Byte reworkStatus;


}
