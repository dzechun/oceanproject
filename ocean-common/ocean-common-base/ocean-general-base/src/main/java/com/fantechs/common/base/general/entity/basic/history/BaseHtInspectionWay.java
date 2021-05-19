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
 * 检验方式履历表
 * base_ht_inspection_way
 * @author admin
 * @date 2021-05-19 09:24:41
 */
@Data
@Table(name = "base_ht_inspection_way")
public class BaseHtInspectionWay extends ValidGroup implements Serializable {
    /**
     * 检验方式履历ID
     */
    @ApiModelProperty(name="htInspectionWayId",value = "检验方式履历ID")
    @Excel(name = "检验方式履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_inspection_way_id")
    private Long htInspectionWayId;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Excel(name = "检验方式ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 检验方式编码
     */
    @ApiModelProperty(name="inspectionWayCode",value = "检验方式编码")
    @Excel(name = "检验方式编码", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_way_code")
    private String inspectionWayCode;

    /**
     * 检验方式描述
     */
    @ApiModelProperty(name="inspectionWayDesc",value = "检验方式描述")
    @Excel(name = "检验方式描述", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_way_desc")
    private String inspectionWayDesc;

    /**
     * 检验类型ID
     */
    @ApiModelProperty(name="inspectionTypeId",value = "检验类型ID")
    @Excel(name = "检验类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_type_id")
    private Long inspectionTypeId;

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
     * 检验类型
     */
    @ApiModelProperty(name="inspectionTypeName" ,value="检验类型")
    @Transient
    @Excel(name = "检验类型", height = 20, width = 30,orderNum="3")
    private String inspectionTypeName;

    private static final long serialVersionUID = 1L;
}