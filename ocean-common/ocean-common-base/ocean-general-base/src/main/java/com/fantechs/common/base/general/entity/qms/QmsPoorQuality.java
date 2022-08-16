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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 品质不良表
 * qms_poor_quality
 * @author jbb
 * @date 2021-01-19 17:47:26
 */
@Data
@Table(name = "qms_poor_quality")
public class QmsPoorQuality extends ValidGroup implements Serializable {
    /**
     * 品质不良ID
     */
    @ApiModelProperty(name="poorQualityId",value = "品质不良ID")
    @Excel(name = "品质不良ID", height = 20, width = 30)
    @Id
    @Column(name = "poor_quality_id")
    private Long poorQualityId;

    /**
     * 品质ID（品质确认ID或者品质抽检ID）
     */
    @ApiModelProperty(name="qualityId",value = "品质ID（品质确认ID或者品质抽检ID）")
    @Excel(name = "品质ID（品质确认ID或者品质抽检ID）", height = 20, width = 30)
    @Column(name = "quality_id")
    private Long qualityId;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId",value = "工段ID")
    @Excel(name = "工段ID", height = 20, width = 30)
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 不良现象ID
     */
    @ApiModelProperty(name="badItemDetId",value = "不良现象ID")
    @Excel(name = "不良现象ID", height = 20, width = 30)
    @Column(name = "bad_item_det_id")
    private Long badItemDetId;

    /**
     * 不良数量
     */
    @ApiModelProperty(name="badQuantity",value = "不良数量")
    @Excel(name = "不良数量", height = 20, width = 30)
    @Column(name = "bad_quantity")
    private BigDecimal badQuantity;

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
