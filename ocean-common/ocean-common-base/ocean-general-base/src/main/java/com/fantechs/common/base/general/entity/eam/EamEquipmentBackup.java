package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 设备备用件
 * eam_equipment_backup
 * @author Dylan
 * @date 2021-09-16 16:57:13
 */
@Data
@Table(name = "eam_equipment_backup")
public class EamEquipmentBackup extends ValidGroup implements Serializable {
    /**
     * 设备备用件ID
     */
    @ApiModelProperty(name="equipmentBackupId",value = "设备备用件ID")
    @Excel(name = "设备备用件ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "equipment_backup_id")
    private Long equipmentBackupId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 备用件编码
     */
    @ApiModelProperty(name="equipmentBackupCode",value = "备用件编码")
    @Excel(name = "备用件编码", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_backup_code")
    private String equipmentBackupCode;

    /**
     * 备用件名称
     */
    @ApiModelProperty(name="equipmentBackupName",value = "备用件名称")
    @Excel(name = "备用件名称", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_backup_name")
    private String equipmentBackupName;

    /**
     * 备用件描述
     */
    @ApiModelProperty(name="equipmentBackupDesc",value = "备用件描述")
    @Excel(name = "备用件描述", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_backup_desc")
    private String equipmentBackupDesc;

    /**
     * 备用件型号
     */
    @ApiModelProperty(name="equipmentBackupModel",value = "备用件型号")
    @Excel(name = "备用件型号", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_backup_model")
    private String equipmentBackupModel;

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
     * 工作区域ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区域ID")
    @Excel(name = "工作区域ID", height = 20, width = 30,orderNum="") 
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
     * 长
     */
    @ApiModelProperty(name="length",value = "长")
    @Excel(name = "长", height = 20, width = 30,orderNum="") 
    private String length;

    /**
     * 宽
     */
    @ApiModelProperty(name="width",value = "宽")
    @Excel(name = "宽", height = 20, width = 30,orderNum="") 
    private String width;

    /**
     * 高
     */
    @ApiModelProperty(name="height",value = "高")
    @Excel(name = "高", height = 20, width = 30,orderNum="") 
    private String height;

    /**
     * 体积
     */
    @ApiModelProperty(name="volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="") 
    private String volume;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}