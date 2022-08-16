package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 设备台账附件履历表
 * eam_ht_equipment_standing_book_attachment
 * @author Dylan
 * @date 2021-08-20 14:28:58
 */
@Data
@Table(name = "eam_ht_equipment_standing_book_attachment")
public class EamHtEquipmentStandingBookAttachment extends ValidGroup implements Serializable {
    /**
     * 设备台账附件履历ID
     */
    @ApiModelProperty(name="htEquipmentStandingBookAttachmentId",value = "设备台账附件履历ID")
    @Excel(name = "设备台账附件履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_standing_book_attachment_id")
    private Long htEquipmentStandingBookAttachmentId;

    /**
     * 设备台账附件ID
     */
    @ApiModelProperty(name="equipmentStandingBookAttachmentId",value = "设备台账附件ID")
    @Excel(name = "设备台账附件ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_standing_book_attachment_id")
    private Long equipmentStandingBookAttachmentId;

    /**
     * 设备台账管理ID
     */
    @ApiModelProperty(name="equipmentStandingBookId",value = "设备台账管理ID")
    @Excel(name = "设备台账管理ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_standing_book_id")
    private Long equipmentStandingBookId;

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