package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 治具台账管理
 * eam_jig_standing_book
 * @author Dylan
 * @date 2021-07-28 11:17:45
 */
@Data
@Table(name = "eam_jig_standing_book")
public class EamJigStandingBook extends ValidGroup implements Serializable {
    /**
     * 治具台账管理ID
     */
    @ApiModelProperty(name="jigStandingBookId",value = "治具台账管理ID")
    @Id
    @Column(name = "jig_standing_book_id")
    @NotNull(groups = update.class,message = "治具台账管理ID不能为空")
    private Long jigStandingBookId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Column(name = "jig_barcode_id")
    private Long jigBarcodeId;

    /**
     * 财产编码类别(1-固定资产  2-列管品)
     */
    @ApiModelProperty(name="propertyCodeCategory",value = "财产编码类别(1-固定资产  2-列管品)")
    @Excel(name = "财产编码类别(1-固定资产  2-列管品)", height = 20, width = 30,orderNum="7",replace = {"固定资产_1","列管品_2"},needMerge = true)
    @Column(name = "property_code_category")
    private Byte propertyCodeCategory;

    /**
     * 出厂日期
     */
    @ApiModelProperty(name="releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="8",needMerge = true)
    @Column(name = "release_date")
    private Date releaseDate;

    /**
     * 部门
     */
    @ApiModelProperty(name="deptId",value = "部门")
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 折旧年限
     */
    @ApiModelProperty(name="depreciableLife",value = "折旧年限")
    @Excel(name = "折旧年限", height = 20, width = 30,orderNum="10",needMerge = true)
    @Column(name = "depreciable_life")
    private Integer depreciableLife;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 附件信息
     */
    @ApiModelProperty(name="eamJigStandingBookAttachmentList",value = "附件信息")
    @ExcelCollection(name="附件信息",orderNum="17")
    private List<EamJigStandingBookAttachment> eamJigStandingBookAttachmentList = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}