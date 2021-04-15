package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/4/7
 */
@Data
public class LabelRuteDto implements Serializable {

    /**
     * 条码/流转卡 解析码
     */
    @Transient
    @ApiModelProperty(name = "barcodeRule",value = "解析码")
    private String barcodeRule;


    /**
     * 条码规则id
     */
    private Long barcodeRuleId;

    /**
     * 标签编码
     */
    @ApiModelProperty(name = "labelCode",value = "标签编码")
    private String labelCode;

    /**
     * 标签模版
     */
    @ApiModelProperty(name = "labelName",value = "标签模版")
    private String labelName;

    /**
     * 标签版本
     */
    @ApiModelProperty(name = "labelVersion",value = "标签版本")
    private String labelVersion;

    /**
     * 条码类型(0.SN,1.CSN)
     */
    @ApiModelProperty(name = "barcodeType",value = "条码类型(0.SN,1.CSN)")
    private Byte barcodeType;

    /**
     * 打印方式
     */
    @Transient
    @ApiModelProperty(name = "printMode",value = "打印方式")
    private String printMode;
}
