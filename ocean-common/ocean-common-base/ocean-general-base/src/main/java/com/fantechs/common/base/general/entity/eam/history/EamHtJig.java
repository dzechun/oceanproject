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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 治具信息履历表
 * eam_ht_jig
 * @author admin
 * @date 2021-07-28 10:57:00
 */
@Data
@Table(name = "eam_ht_jig")
public class EamHtJig extends ValidGroup implements Serializable {
    /**
     * 治具信息履历ID
     */
    @ApiModelProperty(name="htJigId",value = "治具信息履历ID")
    @Excel(name = "治具信息履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_id")
    private Long htJigId;

    /**
     * 治具信息ID
     */
    @ApiModelProperty(name="jigId",value = "治具信息ID")
    @Excel(name = "治具信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_code")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_name")
    private String jigName;

    /**
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_desc")
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_model")
    private String jigModel;

    /**
     * 治具类别ID
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别ID")
    @Excel(name = "治具类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_category_id")
    private Long jigCategoryId;

    /**
     * 治具管理员
     */
    @ApiModelProperty(name="userId",value = "治具管理员")
    @Excel(name = "治具管理员", height = 20, width = 30,orderNum="") 
    @Column(name = "user_id")
    private Long userId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="warehouseAreaId",value = "区域ID")
    @Excel(name = "区域ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_area_id")
    private Long warehouseAreaId;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区ID")
    @Column(name = "working_area_id")
    private Long workingAreaId;

    /**
     * 推荐库位
     */
    @ApiModelProperty(name="storageId",value = "推荐库位")
    @Excel(name = "推荐库位", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 最大使用次数
     */
    @ApiModelProperty(name="maxUsageTime",value = "最大使用次数")
    @Excel(name = "最大使用次数", height = 20, width = 30,orderNum="") 
    @Column(name = "max_usage_time")
    private Integer maxUsageTime;

    /**
     * 警告次数
     */
    @ApiModelProperty(name="warningTime",value = "警告次数")
    @Excel(name = "警告次数", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_time")
    private Integer warningTime;

    /**
     * 最大使用天数
     */
    @ApiModelProperty(name="maxUsageDays",value = "最大使用天数")
    @Excel(name = "最大使用天数", height = 20, width = 30,orderNum="") 
    @Column(name = "max_usage_days")
    private Integer maxUsageDays;

    /**
     * 警告天数
     */
    @ApiModelProperty(name="warningDays",value = "警告天数")
    @Excel(name = "警告天数", height = 20, width = 30,orderNum="") 
    @Column(name = "warning_days")
    private Integer warningDays;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    @Excel(name = "长(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Excel(name = "宽(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Excel(name = "高(cm)", height = 20, width = 30,orderNum="") 
    private BigDecimal height;

    /**
     * 体积(cm3)
     */
    @ApiModelProperty(name="volume",value = "体积(cm3)")
    @Excel(name = "体积(cm3)", height = 20, width = 30,orderNum="") 
    private BigDecimal volume;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30,orderNum="") 
    private BigDecimal weight;

    /**
     * 功率(kw)
     */
    @ApiModelProperty(name="power",value = "功率(kw)")
    @Excel(name = "功率(kw)", height = 20, width = 30,orderNum="") 
    private BigDecimal power;

    /**
     * 保养周期(天)
     */
    @ApiModelProperty(name="maintainCycle",value = "保养周期(天)")
    @Excel(name = "保养周期(天)", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_cycle")
    private Integer maintainCycle;

    /**
     * 保养警告天数
     */
    @ApiModelProperty(name="maintainWarningDays",value = "保养警告天数")
    @Excel(name = "保养警告天数", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_warning_days")
    private Integer maintainWarningDays;

    /**
     * 备用件名称
     */
    @ApiModelProperty(name="backupName",value = "备用件名称")
    @Excel(name = "备用件名称", height = 20, width = 30,orderNum="") 
    @Column(name = "backup_name")
    private String backupName;

    /**
     * 备用件数量
     */
    @ApiModelProperty(name="backupQty",value = "备用件数量")
    @Excel(name = "备用件数量", height = 20, width = 30,orderNum="") 
    @Column(name = "backup_qty")
    private Integer backupQty;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="5")
    private String jigCategoryName;

    /**
     * 治具管理员
     */
    @Transient
    @ApiModelProperty(name = "userName",value = "治具管理员")
    @Excel(name = "治具管理员", height = 20, width = 30,orderNum="6")
    private String userName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 库区
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区")
    private String warehouseAreaName;

    /**
     * 工作区
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区")
    private String workingAreaCode;

    /**
     * 推荐库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "推荐库位")
    private String storageCode;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}