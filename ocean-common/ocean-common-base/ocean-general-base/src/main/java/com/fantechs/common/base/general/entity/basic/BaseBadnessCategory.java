package com.fantechs.common.base.general.entity.basic;

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
 * 不良类别代码
 * base_badness_category
 * @author jbb
 * @date 2021-04-02 15:39:15
 */
@Data
@Table(name = "base_badness_category")
public class BaseBadnessCategory extends ValidGroup implements Serializable {
    /**
     * 不良类别代码ID
     */
    @ApiModelProperty(name="badnessCategoryId",value = "不良类别代码ID")
    @Id
    @Column(name = "badness_category_id")
    private Long badnessCategoryId;

    /**
     * 不良类别代码
     */
    @ApiModelProperty(name="badnessCategoryCode",value = "不良类别代码")
    @Excel(name = "不良类别代码", height = 20, width = 30,orderNum="1")
    @Column(name = "badness_category_code")
    private String badnessCategoryCode;

    /**
     * 不良类别描述
     */
    @ApiModelProperty(name="badnessCategoryDesc",value = "不良类别描述")
    @Excel(name = "不良类别描述", height = 20, width = 30,orderNum="2")
    @Column(name = "badness_category_desc")
    private String badnessCategoryDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="3")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
