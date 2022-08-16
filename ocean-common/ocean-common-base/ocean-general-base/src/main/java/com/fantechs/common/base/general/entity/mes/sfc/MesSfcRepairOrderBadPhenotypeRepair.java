package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 车间管理-维修单不良现象维修表
 * mes_sfc_repair_order_bad_phenotype_repair
 * @author admin
 * @date 2021-09-10 11:18:50
 */
@Data
@Table(name = "mes_sfc_repair_order_bad_phenotype_repair")
public class MesSfcRepairOrderBadPhenotypeRepair extends ValidGroup implements Serializable {
    /**
     * 维修单不良现象维修表ID
     */
    @ApiModelProperty(name="repairOrderBadPhenotypeRepairId",value = "维修单不良现象维修表ID")
    @Excel(name = "维修单不良现象维修表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "repair_order_bad_phenotype_repair_id")
    private Long repairOrderBadPhenotypeRepairId;

    /**
     * 维修单ID
     */
    @ApiModelProperty(name="repairOrderId",value = "维修单ID")
    @Excel(name = "维修单ID", height = 20, width = 30,orderNum="")
    @Column(name = "repair_order_id")
    private Long repairOrderId;

    /**
     * 不良原因ID
     */
    @ApiModelProperty(name="badnessCauseId",value = "不良原因ID")
    @Excel(name = "不良原因ID", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_cause_id")
    private Long badnessCauseId;

    /**
     * 不良责任代码ID
     */
    @ApiModelProperty(name="badnessDutyId",value = "不良责任代码ID")
    @Excel(name = "不良责任代码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_duty_id")
    private Long badnessDutyId;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="") 
    @Column(name = "location_num")
    private String locationNum;

    /**
     * 替换件ID
     */
    @ApiModelProperty(name="replaceMaterialId",value = "替换件ID")
    @Excel(name = "替换件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "replace_material_id")
    private Long replaceMaterialId;

    /**
     * 替换件数量
     */
    @ApiModelProperty(name="replacementQty",value = "替换件数量")
    @Excel(name = "替换件数量", height = 20, width = 30,orderNum="") 
    @Column(name = "replacement_qty")
    private BigDecimal replacementQty;

    /**
     * 替换件条码
     */
    @ApiModelProperty(name="replacementBarcode",value = "替换件条码")
    @Excel(name = "替换件条码", height = 20, width = 30,orderNum="") 
    @Column(name = "replacement_barcode")
    private String replacementBarcode;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 不良原因编码
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseCode",value = "不良原因编码")
    @Excel(name = "不良原因编码", height = 20, width = 30,orderNum="11")
    private String badnessCauseCode;

    /**
     * 不良原因描述
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseDesc",value = "不良原因描述")
    @Excel(name = "不良原因描述", height = 20, width = 30,orderNum="11")
    private String badnessCauseDesc;

    /**
     * 不良责任编码
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyCode",value = "不良责任编码")
    @Excel(name = "不良责任编码", height = 20, width = 30,orderNum="11")
    private String badnessDutyCode;

    /**
     * 不良责任描述
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyDesc",value = "不良责任描述")
    @Excel(name = "不良责任描述", height = 20, width = 30,orderNum="11")
    private String badnessDutyDesc;

    /**
     * 替换零件料号
     */
    @Transient
    @ApiModelProperty(name = "replaceMaterialCode",value = "替换零件料号")
    @Excel(name = "替换零件料号", height = 20, width = 30,orderNum="11")
    private String replaceMaterialCode;

    /**
     * 替换零件描述
     */
    @Transient
    @ApiModelProperty(name = "replaceMaterialDesc",value = "替换零件描述")
    @Excel(name = "替换零件描述", height = 20, width = 30,orderNum="11")
    private String replaceMaterialDesc;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}