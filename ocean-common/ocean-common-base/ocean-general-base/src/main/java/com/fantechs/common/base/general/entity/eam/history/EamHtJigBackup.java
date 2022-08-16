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
 * 治具备用件履历表
 * eam_ht_jig_backup
 * @author admin
 * @date 2021-08-11 09:32:19
 */
@Data
@Table(name = "eam_ht_jig_backup")
public class EamHtJigBackup extends ValidGroup implements Serializable {
    /**
     * 治具备用件履历ID
     */
    @ApiModelProperty(name="htJigBackupId",value = "治具备用件履历ID")
    @Excel(name = "治具备用件履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_jig_backup_id")
    private Long htJigBackupId;

    /**
     * 治具备用件ID
     */
    @ApiModelProperty(name="jigBackupId",value = "治具备用件ID")
    @Excel(name = "治具备用件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_backup_id")
    private Long jigBackupId;

    /**
     * 备用件编码
     */
    @ApiModelProperty(name="jigBackupCode",value = "备用件编码")
    @Excel(name = "备用件编码", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_backup_code")
    private String jigBackupCode;

    /**
     * 备用件名称
     */
    @ApiModelProperty(name="jigBackupName",value = "备用件名称")
    @Excel(name = "备用件名称", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_backup_name")
    private String jigBackupName;

    /**
     * 备用件描述
     */
    @ApiModelProperty(name="jigBackupDesc",value = "备用件描述")
    @Excel(name = "备用件描述", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_backup_desc")
    private String jigBackupDesc;

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
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Excel(name = "库位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="") 
    private Integer qty;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="6")
    private String warehouseName;

    /**
     * 库区名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区名称")
    @Excel(name = "库区名称", height = 20, width = 30,orderNum="6")
    private String warehouseAreaName;

    /**
     * 工作区
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区")
    @Excel(name = "工作区", height = 20, width = 30,orderNum="6")
    private String workingAreaCode;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="6")
    private String storageCode;


    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}