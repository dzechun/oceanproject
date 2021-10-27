package com.fantechs.common.base.general.dto.leisai.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class LeisaiProductAndHalfOrderImport implements Serializable {


    /**
     * 单据号
     */
    @ApiModelProperty(name="productAndHalfOrderCode",value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30)
    private String productAndHalfOrderCode;

    /**
     * 扫描SN
     */
    @ApiModelProperty(name="scanSn",value = "扫描SN")
    @Excel(name = "扫描SN", height = 20, width = 30)
    private String scanSn;

    /**
     * 产品SN
     */
    @ApiModelProperty(name="productSn",value = "产品SN")
    @Excel(name = "产品SN", height = 20, width = 30)
    private String productSn;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    @Excel(name = "生产订单", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="productMaterialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String productMaterialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="productMaterialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30)
    private String productMaterialDesc;

    /**
     * 半成品SN
     */
    @ApiModelProperty(name="halfProductSn",value = "半成品SN")
    @Excel(name = "半成品SN", height = 20, width = 30)
    private String halfProductSn;

    /**
     * 委外订单
     */
    @ApiModelProperty(name="outsourceOrderCode",value = "委外订单")
    @Excel(name = "委外订单", height = 20, width = 30)
    private String outsourceOrderCode;

    /**
     * IPQC检验日期
     */
    @ApiModelProperty(name="ipqcInspectionTime",value = "IPQC检验日期")
    @Excel(name = "IPQC检验日期", height = 20, width = 30)
    private Date ipqcInspectionTime;

    /**
     * 半成品料号
     */
    @ApiModelProperty(name="halfProductMaterialCode",value = "半成品料号")
    @Excel(name = "半成品料号", height = 20, width = 30)
    private String halfProductMaterialCode;

    /**
     * 半成品描述
     */
    @ApiModelProperty(name="halfProductMaterialDesc",value = "半成品描述")
    @Excel(name = "半成品描述", height = 20, width = 30)
    private String halfProductMaterialDesc;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    @Excel(name = "记录人", height = 20, width = 30)
    private String recorder;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30)
    private BigDecimal qty;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date createTime;

}
