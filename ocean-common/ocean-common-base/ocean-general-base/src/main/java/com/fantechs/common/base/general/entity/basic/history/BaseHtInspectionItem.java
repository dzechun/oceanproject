package com.fantechs.common.base.general.entity.basic.history;

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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 检验项目履历表
 * base_ht_inspection_item
 * @author admin
 * @date 2021-06-03 16:26:34
 */
@Data
@Table(name = "base_ht_inspection_item")
public class BaseHtInspectionItem extends ValidGroup implements Serializable {
    /**
     * 检验项目历史ID
     */
    @ApiModelProperty(name="htInspectionItemId",value = "检验项目历史ID")
    @Excel(name = "检验项目历史ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_inspection_item_id")
    private Long htInspectionItemId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty(name="inspectionItemId",value = "检验项目ID")
    @Excel(name = "检验项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_item_id")
    private Long inspectionItemId;

    /**
     * 父ID
     */
    @ApiModelProperty(name="parentId",value = "父ID")
    @Excel(name = "父ID", height = 20, width = 30,orderNum="")
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 检验项目编码
     */
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目编码")
    @Excel(name = "检验项目编码", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_code")
    private String inspectionItemCode;

    /**
     * 检验项目标准
     */
    @ApiModelProperty(name="inspectionItemStandard",value = "检验项目标准")
    @Excel(name = "检验项目标准", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_standard")
    private String inspectionItemStandard;

    /**
     * 检验项目类型(1-大类 2-小类)
     */
    @ApiModelProperty(name="inspectionItemType",value = "检验项目类型(1-大类 2-小类)")
    @Excel(name = "检验项目类型(1-大类 2-小类)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_type")
    private Byte inspectionItemType;

    /**
     * 检验项目描述
     */
    @ApiModelProperty(name="inspectionItemDesc",value = "检验项目描述")
    @Excel(name = "检验项目描述", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_item_desc")
    private String inspectionItemDesc;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    private static final long serialVersionUID = 1L;
}