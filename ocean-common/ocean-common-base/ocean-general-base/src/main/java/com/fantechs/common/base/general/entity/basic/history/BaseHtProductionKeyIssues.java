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
import java.util.Date;

;
;

/**
 * 产品关键事项维护履历表
 * base_ht_production_key_issues
 * @author admin
 * @date 2021-06-10 19:22:57
 */
@Data
@Table(name = "base_ht_production_key_issues")
public class BaseHtProductionKeyIssues extends ValidGroup implements Serializable {
    /**
     * 产品关键事项维护履历ID
     */
    @ApiModelProperty(name="htProductionKeyIssuesId",value = "产品关键事项维护履历ID")
    @Excel(name = "产品关键事项维护履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_production_key_issues_id")
    private Long htProductionKeyIssuesId;

    /**
     * 产品关键事项维护ID
     */
    @ApiModelProperty(name="productionKeyIssuesId",value = "产品关键事项维护ID")
    @Excel(name = "产品关键事项维护ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_key_issues_id")
    private Long productionKeyIssuesId;

    /**
     * 关键事项维度(1-物料 2-通用)
     */
    @ApiModelProperty(name="keyIssuesType",value = "关键事项维度(1-物料 2-通用)")
    @Excel(name = "关键事项维度(1-物料 2-通用)", height = 20, width = 30,orderNum="") 
    @Column(name = "key_issues_type")
    private Byte keyIssuesType;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

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
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="5")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 产品型号编码
     */
    @ApiModelProperty(name="materialCode" ,value="产品型号编码")
    @Transient
    @Excel(name = "产品型号编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName" ,value="产品名称")
    @Transient
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 产品型号描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品型号描述")
    @Transient
    @Excel(name = "产品型号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion" ,value="产品版本")
    @Transient
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="5")
    private String materialVersion;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name="materialProperty" ,value="物料属性(0.半成品，1.成品)")
    @Transient
    @Excel(name = "物料属性(0.半成品，1.成品)", height = 20, width = 30,orderNum="6")
    private Byte materialProperty;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}