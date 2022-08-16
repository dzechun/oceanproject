package com.fantechs.common.base.general.entity.qms.history;

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
 * 不良项目历史表
 * @date 2021-01-27 09:39:33
 */
@Data
@Table(name = "qms_ht_bad_item")
public class QmsHtBadItem extends ValidGroup implements Serializable {
    /**
     * 不良项目历史ID
     */
    @ApiModelProperty(name="htBadItemId",value = "不良项目历史ID")
    @Id
    @Column(name = "ht_bad_item_id")
    private Long htBadItemId;

    /**
     * 不良项目ID
     */
    @ApiModelProperty(name="badItemId",value = "不良项目ID")
    @Column(name = "bad_item_id")
    private Long badItemId;

    /**
     * 不良类型编码
     */
    @ApiModelProperty(name="badTypeCode",value = "不良类型编码")
    @Excel(name = "不良项目编码", height = 20, width = 30,orderNum="1")
    @Column(name = "bad_type_code")
    private String badTypeCode;

    /**
     * 不良类型原因
     */
    @ApiModelProperty(name="badTypeCause",value = "不良类型原因")
    @Excel(name = "不良类型原因", height = 20, width = 30,orderNum="2")
    @Column(name = "bad_type_cause")
    private String badTypeCause;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId",value = "工段ID")
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="4")
    private String processName;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="6")
    private String sectionName;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}
