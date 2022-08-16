package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseProductBomImport implements Serializable {

    /**
     * 产品BOM编号
     */
    @ApiModelProperty(name = "productBomCode", value = "产品BOM编号")
    @Excel(name = "产品BOM编号(必填)", height = 20, width = 30)
    private String productBomCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long proLineId;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "线别编码", height = 20, width = 30)
    private String proCode;

    /**
     * 代用物料Id
     */
    @ApiModelProperty(name="subMaterialId" ,value="代用物料Id")
    private Long subMaterialId;

    /**
     * 代用物料编码
     */
    @ApiModelProperty(name="subMaterialCode" ,value="代用物料编码")
    @Excel(name = "代用物料编码", height = 20, width = 30)
    private String subMaterialCode;

    /**
     * 用量
     */
    @ApiModelProperty(name="quantity" ,value="用量")
    @Excel(name = "用量", height = 20, width = 30)
    private BigDecimal quantity;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQuantity" ,value="基准数量")
    @Excel(name = "基准数量", height = 20, width = 30)
    private BigDecimal baseQuantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    @Excel(name = "位置", height = 20, width = 30)
    private String position;

    /**
     * 工序Id
     */
    @ApiModelProperty(name="processId" ,value="工序Id")
    private Long processId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode" ,value="工序编码")
    @Excel(name = "工序编码", height = 20, width = 30)
    private String processCode;

    /**
     * 配送方式（1、配送优化 2、配送不优化  3、不配送不优化）
     */
    @ApiModelProperty(name = "deliveryMode", value = "配送方式（1、配送优化 2、配送不优化  3、不配送不优化）")
    @Excel(name = "配送方式（1、配送优化 2、配送不优化  3、不配送不优化）", height = 20, width = 30)
    private Integer deliveryMode;

    /**
     * 发料方式（1、推式  2、拉式）
     */
    @ApiModelProperty(name = "issueMethod", value = "发料方式（1、推式  2、拉式）")
    @Excel(name = "发料方式（1、推式  2、拉式）", height = 20, width = 30)
    private Integer issueMethod;

    /**
     * 损耗率
     */
    @ApiModelProperty(name = "lossRate", value = "损耗率")
    @Excel(name = "损耗率", height = 20, width = 30)
    private BigDecimal lossRate;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name = "takt", value = "节拍数量(秒)")
    @Excel(name = "节拍数量(秒)", height = 20, width = 30)
    private Integer takt;

    /**
     * 移转数量
     */
    @ApiModelProperty(name = "transferQuantity", value = "移转数量")
    @Excel(name = "移转数量", height = 20, width = 30)
    private Integer transferQuantity;

    /**
     * BOM状态(1-未核准 2-已核准)
     */
    @ApiModelProperty(name = "bomStatus", value = "BOM状态(1-未核准 2-已核准)")
    @Excel(name = "BOM状态(1-未核准 2-已核准)", height = 20, width = 30)
    private Byte bomStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 父BOM编码
     */
    @ApiModelProperty(name = "parentProductBomCode", value = "父BOM编码")
    @Excel(name = "父BOM编码", height = 20, width = 30)
    private String parentProductBomCode;
}
