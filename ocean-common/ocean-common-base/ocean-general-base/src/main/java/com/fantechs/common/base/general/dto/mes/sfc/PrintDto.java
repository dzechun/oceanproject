package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2021/3/24
 */

/**
 * 选择打印机实体
 */
@Data
public class PrintDto implements Serializable {
    /**
     * 打印机名称
     */
    @ApiModelProperty("打印机名称")
    private String printName;
    /**
     * 标签模版
     */
    @ApiModelProperty("标签名称")
    private String LabelName;

    /**
     * 标签版本
     */
    @ApiModelProperty("版本")
    private String labelVersion;

    /**
     * 打印数据
     */
    private List<PrintModel> printModelList;
}
