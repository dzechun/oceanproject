package com.fantechs.common.base.entity.basic.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

@Table(name = "wms_in_ht_storage_bills")
@Data
public class WmsInHtStorageBills implements Serializable {
    /**
    * 履历仓库清单id
    */
    @ApiModelProperty(value = "履历仓库清单id",example = "履历仓库清单id")
    @Column(name = "ht_storage_bills_id")
    @Id
    @Excel(name = "履历仓库清单id")
    private Long htStorageBillsId;

    /**
     * 仓库清单id
     */
    @ApiModelProperty(value = "仓库清单id",example = "仓库清单id")
    @Column(name = "storage_bills_id")
    @Excel(name = "仓库清单id")
    private Long storageBillsId;

    /**
     * 组织代码id
     */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
    * 仓库清单号
    */
    @ApiModelProperty(value = "仓库清单号",example = "仓库清单号")
    @Column(name = "storage_bills_code")
    @Excel(name = "仓库清单号")
    private String storageBillsCode;

    /**
    * 仓库清单类型（1、入库单 2、收货单 3、完工入库单 4、销售退货单）
    */
    @ApiModelProperty(value = "仓库清单类型（1、入库单 2、收货单 3、完工入库单 4、销售退货单）",example = "仓库清单类型（1、入库单 2、收货单 3、完工入库单 4、销售退货单）")
    @Column(name = "bills_type")
    @Excel(name = "仓库清单类型（1、入库单 2、收货单 3、完工入库单 4、销售退货单）")
    private Integer billsType;

    /**
    * 仓库清单数量总计
    */
    @ApiModelProperty(value = "仓库清单数量总计",example = "仓库清单数量总计")
    @Excel(name = "仓库清单数量总计")
    private java.math.BigDecimal total;

    /**
    * 待入库物料种类总计
    */
    @ApiModelProperty(value = "待入库物料种类总计",example = "待入库物料种类总计")
    @Column(name = "material_type_total")
    @Excel(name = "待入库物料种类总计")
    private java.math.BigDecimal materialTypeTotal;

    /**
    * 已入库物料种类总计
    */
    @ApiModelProperty(value = "已入库物料种类总计",example = "已入库物料种类总计")
    @Column(name = "finished_total")
    @Excel(name = "已入库物料种类总计")
    private java.math.BigDecimal finishedTotal;

    /**
    * 仓库清单单据类型（1、收货单 2、入库任务单 3、完工入库单 4、销售退货单）
    */
    @ApiModelProperty(value = "仓库清单单据类型（1、收货单 2、入库任务单 3、完工入库单 4、销售退货单）",example = "仓库清单单据类型（1、收货单 2、入库任务单 3、完工入库单 4、销售退货单）")
    @Excel(name = "仓库清单单据类型（1、收货单 2、入库任务单 3、完工入库单 4、销售退货单）")
    private Integer type;

    /**
    * 供应商或客户Id
    */
    @ApiModelProperty(value = "供应商或客户Id",example = "供应商或客户Id")
    @Column(name = "supplier_id")
    @Excel(name = "供应商或客户Id")
    private Long supplierId;

    /**
    * 仓库清单状态（1、待完成 2、进行中 3、完成 4、未完成）
    */
    @ApiModelProperty(value = "仓库清单状态（1、待完成 2、进行中 3、完成 4、未完成）",example = "仓库清单状态（1、待完成 2、进行中 3、完成 4、未完成）")
    @Excel(name = "仓库清单状态（1、待完成 2、进行中 3、完成 4、未完成）")
    private Byte status;

    /**
    * 是否允许分批次（0、否 1、是）
    */
    @ApiModelProperty(value = "是否允许分批次（0、否 1、是）",example = "是否允许分批次（0、否 1、是）")
    @Column(name = "allow_batch")
    @Excel(name = "是否允许分批次（0、否 1、是）")
    private Byte allowBatch;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
    * 送货人名称
    */
    @ApiModelProperty(value = "送货人名称",example = "送货人名称")
    @Column(name = "send_user_name")
    @Excel(name = "送货人名称")
    private String sendUserName;

    /**
    * 收货人ID
    */
    @ApiModelProperty(value = "收货人ID",example = "收货人ID")
    @Column(name = "accept_user_id")
    @Excel(name = "收货人ID")
    private Long acceptUserId;

    /**
    * 单据时间（看具体需求而定义不同）
    */
    @ApiModelProperty(value = "单据时间（看具体需求而定义不同）",example = "单据时间（看具体需求而定义不同）")
    @Column(name = "bills_time")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "单据时间（看具体需求而定义不同）")
    private java.util.Date billsTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    @Excel(name = "备注")
    private String note;

    /**
     * 操作记录描述
     */
    @ApiModelProperty(value = "操作记录描述",example = "操作记录描述")
    @Excel(name = "备注")
    private String operation;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    @Excel(name = "创建人ID")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    @Excel(name = "修改人ID")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}