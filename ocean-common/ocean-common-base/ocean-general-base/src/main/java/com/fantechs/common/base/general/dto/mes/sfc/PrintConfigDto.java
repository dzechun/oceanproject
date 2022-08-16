package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/4/9
 */
@Data
public class PrintConfigDto implements Serializable {
    /**
     * 标签类别编码
     */
    @ApiModelProperty("标签类别编码")
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty("标签类别名称")
    private String labelCategoryName;

    /**
     * 是否打印(0.否、1.是)
     */
    @ApiModelProperty("是否打印(0.否、1.是)")
    private Byte isPrinting;

    /**
     * 打印张数
     */
    @ApiModelProperty("打印张数")
    private Integer size;

    /**
     * 打印方式
     */
    @ApiModelProperty("打印方式")
    private String printMode;
}
