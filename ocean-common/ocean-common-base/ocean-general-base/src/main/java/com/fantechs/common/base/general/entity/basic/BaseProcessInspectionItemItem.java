package com.fantechs.common.base.general.entity.basic;

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
 * 过程检验项目编码检验项
 * base_process_inspection_item_item
 * @author admin
 * @date 2021-06-02 10:12:13
 */
@Data
@Table(name = "base_process_inspection_item_item")
public class BaseProcessInspectionItemItem extends ValidGroup implements Serializable {
    /**
     * 过程检验项目检验项ID
     */
    @ApiModelProperty(name="processInspectionItemItemId",value = "过程检验项目检验项ID")
    @Excel(name = "过程检验项目检验项ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "process_inspection_item_item_id")
    private Long processInspectionItemItemId;

    /**
     * 过程检验项目ID
     */
    @ApiModelProperty(name="processInspectionItemId",value = "过程检验项目ID")
    @Excel(name = "过程检验项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_inspection_item_id")
    private Long processInspectionItemId;

    /**
     * 过程检验项目名称
     */
    @ApiModelProperty(name="processInspectionItemItemName",value = "过程检验项目名称")
    @Excel(name = "过程检验项目名称", height = 20, width = 30,orderNum="") 
    @Column(name = "process_inspection_item_item_name")
    private String processInspectionItemItemName;

    /**
     * 过程检验项目内容
     */
    @ApiModelProperty(name="processInspectionItemItemContent",value = "过程检验项目内容")
    @Excel(name = "过程检验项目内容", height = 20, width = 30,orderNum="") 
    @Column(name = "process_inspection_item_item_content")
    private String processInspectionItemItemContent;

    /**
     * 标准
     */
    @ApiModelProperty(name="standard",value = "标准")
    @Excel(name = "标准", height = 20, width = 30,orderNum="") 
    private String standard;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

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