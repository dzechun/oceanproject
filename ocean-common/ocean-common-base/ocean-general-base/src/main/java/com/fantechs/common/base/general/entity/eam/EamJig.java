package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.eam.EamJigBackupDto;
import com.fantechs.common.base.general.dto.eam.EamSparePartReJigDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 治具信息
 * eam_jig
 * @author admin
 * @date 2021-07-28 09:12:19
 */
@Data
@Table(name = "eam_jig")
public class EamJig extends ValidGroup implements Serializable {
    /**
     * 治具信息ID
     */
    @ApiModelProperty(name="jigId",value = "治具信息ID")
    @Id
    @Column(name = "jig_id")
    @NotNull(groups = update.class,message = "治具信息ID不能为空")
    private Long jigId;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="1")
    @Column(name = "jig_code")
    @NotBlank(message = "治具编码不能为空")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="2")
    @Column(name = "jig_name")
    @NotBlank(message = "治具名称不能为空")
    private String jigName;

    /**
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="3")
    @Column(name = "jig_desc")
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="4")
    @Column(name = "jig_model")
    private String jigModel;

    /**
     * 治具类别ID
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别ID")
    @Column(name = "jig_category_id")
    @NotNull(message = "治具类别不能为空")
    private Long jigCategoryId;

    /**
     * 治具管理员
     */
    @ApiModelProperty(name="userId",value = "治具管理员")
    @Column(name = "user_id")
    private Long userId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="warehouseAreaId",value = "区域ID")
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
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 保养最大使用次数
     */
    @ApiModelProperty(name="maintainMaxUsageTime",value = "保养最大使用次数")
    @Column(name = "maintain_max_usage_time")
    private Integer maintainMaxUsageTime;

    /**
     * 保养警告次数
     */
    @ApiModelProperty(name="maintainWarningTime",value = "保养警告次数")
    @Column(name = "maintain_warning_time")
    private Integer maintainWarningTime;

    /**
     * 最大使用次数
     */
    @ApiModelProperty(name="maxUsageTime",value = "最大使用次数")
    @Column(name = "max_usage_time")
    private Integer maxUsageTime;

    /**
     * 警告次数
     */
    @ApiModelProperty(name="warningTime",value = "警告次数")
    @Column(name = "warning_time")
    private Integer warningTime;

    /**
     * 最大使用天数
     */
    @ApiModelProperty(name="maxUsageDays",value = "最大使用天数")
    @Column(name = "max_usage_days")
    private Integer maxUsageDays;

    /**
     * 警告天数
     */
    @ApiModelProperty(name="warningDays",value = "警告天数")
    @Column(name = "warning_days")
    private Integer warningDays;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    private BigDecimal height;

    /**
     * 体积(cm3)
     */
    @ApiModelProperty(name="volume",value = "体积(cm3)")
    private BigDecimal volume;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    private BigDecimal weight;

    /**
     * 功率(kw)
     */
    @ApiModelProperty(name="power",value = "功率(kw)")
    private BigDecimal power;

    /**
     * 保养周期(天)
     */
    @ApiModelProperty(name="maintainCycle",value = "保养周期(天)")
    @Column(name = "maintain_cycle")
    private Integer maintainCycle;

    /**
     * 保养警告天数
     */
    @ApiModelProperty(name="maintainWarningDays",value = "保养警告天数")
    @Column(name = "maintain_warning_days")
    private Integer maintainWarningDays;

    /**
     * 备用件名称
     */
    @ApiModelProperty(name="backupName",value = "备用件名称")
    @Column(name = "backup_name")
    private String backupName;

    /**
     * 备用件数量
     */
    @ApiModelProperty(name="backupQty",value = "备用件数量")
    @Column(name = "backup_qty")
    private Integer backupQty;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="7",replace = {"无效_0","有效_1"})
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
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 条码信息
     */
    @ApiModelProperty(name="eamJigBarcodeList",value = "条码信息")
    private List<EamJigBarcode> eamJigBarcodeList = new ArrayList<>();

    /**
     * 附件信息
     */
    @ApiModelProperty(name="eamJigAttachmentList",value = "附件信息")
    private List<EamJigAttachment> eamJigAttachmentList = new ArrayList<>();

    /**
     * 备用件信息
     */
    @ApiModelProperty(name="eamJigBackupDtoList",value = "备用件信息")
    private List<EamSparePartReJigDto> eamJigBackupDtoList = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}