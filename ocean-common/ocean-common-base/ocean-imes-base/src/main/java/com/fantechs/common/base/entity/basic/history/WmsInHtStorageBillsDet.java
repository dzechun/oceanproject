package com.fantechs.common.base.entity.basic.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

@Table(name = "wms_in_ht_storage_bills_det")
@Data
public class WmsInHtStorageBillsDet implements Serializable {
    /**
    * 履历仓库清单详情id
    */
    @ApiModelProperty(value = "履历仓库清单详情id",example = "履历仓库清单详情id")
    @Column(name = "ht_storage_bills_det_id")
    @Id
    @Excel(name = "履历仓库清单详情id")
    private Long htStorageBillsDetId;

    /**
    * 仓库清单详情id
    */
    @ApiModelProperty(value = "仓库清单详情id",example = "仓库清单详情id")
    @Column(name = "storage_bills_det_id")
    @Excel(name = "仓库清单详情id")
    private Long storageBillsDetId;

    /**
     * 组织代码id
     */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
    * 仓库清单ID
    */
    @ApiModelProperty(value = "仓库清单ID",example = "仓库清单ID")
    @Column(name = "storage_bills_id")
    @Excel(name = "仓库清单ID")
    private Long storageBillsId;

    /**
    * 物料ID
    */
    @ApiModelProperty(value = "物料ID",example = "物料ID")
    @Column(name = "material_id")
    @Excel(name = "物料ID")
    private Long materialId;

    /**
    * 应收总计
    */
    @ApiModelProperty(value = "应收总计",example = "应收总计")
    @Column(name = "will_income_total")
    @Excel(name = "应收总计")
    private java.math.BigDecimal willIncomeTotal;

    /**
    * 实收总计
    */
    @ApiModelProperty(value = "实收总计",example = "实收总计")
    @Column(name = "real_income_total")
    @Excel(name = "实收总计")
    private java.math.BigDecimal realIncomeTotal;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
    * 入库状态（1、待完成 2、进行中 3、完成 4、未完成）
    */
    @ApiModelProperty(value = "入库状态（1、待完成 2、进行中 3、完成 4、未完成）",example = "入库状态（1、待完成 2、进行中 3、完成 4、未完成）")
    @Excel(name = "入库状态（1、待完成 2、进行中 3、完成 4、未完成）")
    private Byte status;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}