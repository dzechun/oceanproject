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
 * 设备备用件设备关系履历表
 * eam_ht_spare_part_re_equ
 * @author admin
 * @date 2021-09-17 17:17:43
 */
@Data
@Table(name = "eam_ht_spare_part_re_equ")
public class EamHtSparePartReEqu extends ValidGroup implements Serializable {
    /**
     * 备用件设备关系履历表ID
     */
    @ApiModelProperty(name="htSparePartReEquId",value = "备用件设备关系履历表ID")
    @Excel(name = "备用件设备关系履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_spare_part_re_equ_id")
    private Long htSparePartReEquId;

    /**
     * 备用件设备关系表ID
     */
    @ApiModelProperty(name="sparePartReEquId",value = "备用件设备关系表ID")
    @Excel(name = "备用件设备关系表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_re_equ_id")
    private Long sparePartReEquId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 备用件ID
     */
    @ApiModelProperty(name="sparePartId",value = "备用件ID")
    @Excel(name = "备用件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_id")
    private Long sparePartId;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="16")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="18")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 备用件编码
     */
    @Transient
    @ApiModelProperty(name = "equipmentBackupCode",value = "备用件编码")
    private String equipmentBackupCode;

    /**
     * 备用件描述
     */
    @ApiModelProperty(name="equipmentBackupDesc",value = "备用件描述")
    @Transient
    private String equipmentBackupDesc;

    /**
     * 备用件名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentBackupName",value = "备用件名称")
    private String equipmentBackupName;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Transient
    private Integer qty;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 库位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位名称")
    private String storageCode;

    /**
     * 工作区编码
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区编码")
    private String workingAreaCode;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}