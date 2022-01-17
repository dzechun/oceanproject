package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class SearchWmsInnerMaterialBarcodeReOrder extends BaseQuery implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    private String colorBoxCode;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;

    /**
     * 单据编号
     */
    @ApiModelProperty(name="orderCode",value = "单据编号")
    private String orderCode;

    /**
     * 单据ID
     */
    @ApiModelProperty(name="orderId",value = "单据ID")
    private Long orderId;

    /**
     * 单据明细ID
     */
    @ApiModelProperty(name="orderDetId",value = "单据明细ID")
    private Long orderDetId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 扫描状态(1-未扫描 2-已保存 3-已提交)
     */
    @ApiModelProperty(name="scanStatus",value = "扫描状态(1-未扫描 2-已保存 3-已提交)")
    private Byte scanStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     * 是否系统条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifSysBarcode",value = "是否系统条码(0-否 1-是)")
    private Byte ifSysBarcode;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 单据明细ID集合
     */
    @ApiModelProperty(name="orderDetIdList",value = "单据明细ID集合")
    private List<Long> orderDetIdList;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    /**
     * 生产时间开始
     */
    @ApiModelProperty(name="productionTimeStart",value = "生产开始时间")
    private String productionTimeStart;

    /**
     * 生产时间结束
     */
    @ApiModelProperty(name="productionTimeEnd",value = "生产时间结束")
    private String productionTimeEnd;

    /**
     * 1-未质检 2-合格 3-不合格
     */
    @ApiModelProperty(name="inspectionStatus",value = "1-未质检 2-合格 3-不合格")
    private Byte inspectionStatus;

    /**
     *全局查（栈板、包箱、sn、彩盒码）
     */
    @ApiModelProperty(name = "overallCode",value = "全局查（栈板、包箱、sn、彩盒码）")
    private String overallCode;


    private static final long serialVersionUID = 1L;
}
