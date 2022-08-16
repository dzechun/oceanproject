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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 设备附件
 * eam_equipment_attachment
 * @author admin
 * @date 2021-08-20 17:06:54
 */
@Data
@Table(name = "eam_equipment_attachment")
public class EamEquipmentAttachment extends ValidGroup implements Serializable {
    /**
     * 设备附件ID
     */
    @ApiModelProperty(name="equipmentAttachmentId",value = "设备附件ID")
    @Excel(name = "设备附件ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "equipment_attachment_id")
    private Long equipmentAttachmentId;

    /**
     * 设备信息ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备信息ID")
    @Excel(name = "设备信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 原文件名
     */
    @ApiModelProperty(name="fileOrgName",value = "原文件名")
    @Excel(name = "原文件名", height = 20, width = 30,orderNum="") 
    @Column(name = "file_org_name")
    private String fileOrgName;

    /**
     * 现文件名
     */
    @ApiModelProperty(name="fileName",value = "现文件名")
    @Excel(name = "现文件名", height = 20, width = 30,orderNum="") 
    @Column(name = "file_name")
    private String fileName;

    /**
     * 存储路径
     */
    @ApiModelProperty(name="storePath",value = "存储路径")
    @Excel(name = "存储路径", height = 20, width = 30,orderNum="") 
    @Column(name = "store_path")
    private String storePath;

    /**
     * 访问路径
     */
    @ApiModelProperty(name="accessUrl",value = "访问路径")
    @Excel(name = "访问路径", height = 20, width = 30,orderNum="") 
    @Column(name = "access_url")
    private String accessUrl;

    /**
     * 文件大小(M)
     */
    @ApiModelProperty(name="fileSize",value = "文件大小(M)")
    @Excel(name = "文件大小(M)", height = 20, width = 30,orderNum="") 
    @Column(name = "file_size")
    private BigDecimal fileSize;

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