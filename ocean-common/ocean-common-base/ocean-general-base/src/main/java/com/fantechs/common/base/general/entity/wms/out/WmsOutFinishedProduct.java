package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

;

/**
 * 成品出库单
 * wms_out_finished_product
 * @author hyc
 * @date 2020-12-22 15:02:48
 */
@Data
@Table(name = "wms_out_finished_product")
public class WmsOutFinishedProduct extends ValidGroup implements Serializable {
    /**
     * 成品出库单ID
     */
    @ApiModelProperty(name="finishedProductId",value = "成品出库单ID")
    @Id
    @Column(name = "finished_product_id")
    @NotNull(groups = update.class,message = "成品出库单ID不能为空")
    private Long finishedProductId;

    /**
     * 成品出库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品出库单号")
    @Excel(name = "成品出库单号", height = 20, width = 30,orderNum="1")
    @Column(name = "finished_product_code")
    private String finishedProductCode;

    /**
     * 供应商Id
     */
    @ApiModelProperty(name="supplierId",value = "供应商Id")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttime",value = "预计出库时间")
    @Excel(name = "预计出库时间", height = 20, width = 30,orderNum="2",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_outtime")
    private Date planOuttime;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttime",value = "实际出库时间")
    @Excel(name = "实际出库时间", height = 20, width = 30,orderNum="3",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reality_outtime")
    private Date realityOuttime;

    /**
     * 计划出库数量
     */
    @ApiModelProperty(name="planOutquantity",value = "计划出库数量")
    @Excel(name = "计划出库数量", height = 20, width = 30,orderNum="4")
    @Column(name = "plan_outquantity")
    private BigDecimal planOutquantity;

    /**
     * 实际出库数量
     */
    @ApiModelProperty(name="realityOutquantity",value = "实际出库数量")
    @Excel(name = "实际出库数量", height = 20, width = 30,orderNum="5")
    @Column(name = "reality_outquantity")
    private BigDecimal realityOutquantity;

    /**
     * 运输方式（0-陆运 1-海运 2-空运
     */
    @ApiModelProperty(name="trafficType",value = "运输方式（0-陆运 1-海运 2-空运")
    @Excel(name = "运输方式（0-陆运 1-海运 2-空运", height = 20, width = 30,orderNum="6",replace = {"陆运_0","海运_1","空运_2"})
    @Column(name = "traffic_type")
    private Byte trafficType;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    @Excel(name = "单据状态（0-待出库 1-出库中 2-出库完成）", height = 20, width = 30,orderNum="7",replace = {"待出库_0","出库中_1","出库完成_2"})
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="8",replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @ApiModelProperty(name="wmsOutFinishedProductDetList",value = "子表明细")
    private List<WmsOutFinishedProductDet> wmsOutFinishedProductDetList;


    private static final long serialVersionUID = 1L;
}