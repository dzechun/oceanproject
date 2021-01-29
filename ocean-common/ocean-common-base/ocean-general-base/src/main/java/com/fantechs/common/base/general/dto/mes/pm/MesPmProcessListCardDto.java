package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2021/1/29
 */
@Data
public class MesPmProcessListCardDto {
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inboundTime",value = "入站时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "inbound_time")
    private Date inboundTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outboundTime",value = "出站时间")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "outbound_time")
    private Date outboundTime;

    /**
     * 处理人
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;

    /**
     * 过站数量
     */
    @ApiModelProperty(name="outputQuantity",value = "报工数量")
    @Column(name = "output_quantity")
    private BigDecimal outputQuantity;

    /**
     * 不合格数量
     */
    @ApiModelProperty(name="unqualifiedQuantity",value = "不合格数量")
    @Column(name = "unqualified_quantity")
    private BigDecimal unqualified_quantity;
}
