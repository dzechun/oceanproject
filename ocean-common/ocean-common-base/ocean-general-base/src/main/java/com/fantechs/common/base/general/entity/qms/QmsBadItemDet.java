package com.fantechs.common.base.general.entity.qms;

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


/**
 * 不良项目明细表
 * @date 2021-01-18 14:19:48
 */
@Data
@Table(name = "qms_bad_item_det")
public class QmsBadItemDet extends ValidGroup implements Serializable {
    /**
     * 不良项目明细ID
     */
    @ApiModelProperty(name="badItemDetId",value = "不良项目明细ID")
    @Excel(name = "不良项目明细ID", height = 20, width = 30)
    @Id
    @Column(name = "bad_item_det_id")
    private Long badItemDetId;

    /**
     * 不良项目ID
     */
    @ApiModelProperty(name="badItemId",value = "不良项目ID")
    @Excel(name = "不良项目ID", height = 20, width = 30)
    @Column(name = "bad_item_id")
    private Long badItemId;

    /**
     * 不良现象编码
     */
    @ApiModelProperty(name="badPhenomenonCode",value = "不良现象编码")
    @Excel(name = "不良现象编码", height = 20, width = 30)
    @Column(name = "bad_phenomenon_code")
    private String badPhenomenonCode;

    /**
     * 不良现象
     */
    @ApiModelProperty(name="badPhenomenon",value = "不良现象")
    @Excel(name = "不良现象", height = 20, width = 30)
    @Column(name = "bad_phenomenon")
    private String badPhenomenon;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
