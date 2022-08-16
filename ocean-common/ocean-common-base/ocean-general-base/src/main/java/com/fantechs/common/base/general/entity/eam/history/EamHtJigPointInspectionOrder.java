package com.fantechs.common.base.general.entity.eam.history;

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
import java.util.Date;

;
;

/**
 * 治具点检单履历表
 * eam_ht_jig_point_inspection_order
 * @author admin
 * @date 2021-08-16 11:36:36
 */
@Data
@Table(name = "eam_ht_jig_point_inspection_order")
public class EamHtJigPointInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 治具点检单履历ID
     */
    @ApiModelProperty(name="htJigPointInspectionOrderId",value = "治具点检单履历ID")
    @Excel(name = "治具点检单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_point_inspection_order_id")
    private Long htJigPointInspectionOrderId;

    /**
     * 治具点检单ID
     */
    @ApiModelProperty(name="jigPointInspectionOrderId",value = "治具点检单ID")
    @Excel(name = "治具点检单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_point_inspection_order_id")
    private Long jigPointInspectionOrderId;

    /**
     * 治具点检单号
     */
    @ApiModelProperty(name="jigPointInspectionOrderCode",value = "治具点检单号")
    @Excel(name = "治具点检单号", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_point_inspection_order_code")
    private String jigPointInspectionOrderCode;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Excel(name = "治具条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 治具点检项目ID
     */
    @ApiModelProperty(name="jigPointInspectionProjectId",value = "治具点检项目ID")
    @Excel(name = "治具点检项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_point_inspection_project_id")
    private Long jigPointInspectionProjectId;

    /**
     * 单据状态(1-待点检 2-已点检)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待点检 2-已点检)")
    @Excel(name = "单据状态(1-待点检 2-已点检)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="2")
    private String jigBarcode;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="3")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="4")
    private String jigName;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="5")
    private String jigCategoryName;

    /**
     * 治具点检项目编码
     */
    @Transient
    @ApiModelProperty(name = "jigPointInspectionProjectCode",value = "治具点检项目编码")
    @Excel(name = "治具点检项目编码", height = 20, width = 30,orderNum="6")
    private String jigPointInspectionProjectCode;

    /**
     * 治具点检项目名称
     */
    @Transient
    @ApiModelProperty(name = "jigPointInspectionProjectName",value = "治具点检项目名称")
    @Excel(name = "治具点检项目名称", height = 20, width = 30,orderNum="7")
    private String jigPointInspectionProjectName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}