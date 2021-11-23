package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.entity.MonthInOutModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/11/23
 */
@Data
public class MonthOutDto extends MonthInOutModel implements Serializable {
    /**
     * 本月出库累计
     */
    @ApiModelProperty(name = "qty",value = "本月出库累计")
    @Excel(name = "本月出库累计",height = 20, width = 30,orderNum="10")
    private BigDecimal qty;

    /**
     * 出库日期
     */
    @ApiModelProperty(name = "outDate",value = "出库日期")
    @Excel(name = "出库日期",exportFormat = "yyyy-MM-dd",height = 20, width = 30,orderNum="11")
    @JSONField(format ="yyyy-MM-dd")
    private Date outDate;
}
