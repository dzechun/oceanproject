package com.fantechs.common.base.general.entity.security;

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
 * 自定义表单
 * sys_default_custom_form
 * @author admin
 * @date 2021-08-27 11:48:43
 */
@Data
@Table(name = "sys_default_custom_form")
public class SysDefaultCustomForm extends ValidGroup implements Serializable {
    /**
     * 自定义表单Id
     */
    @ApiModelProperty(name="customFormId",value = "自定义表单Id")
    @Excel(name = "自定义表单Id", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "custom_form_id")
    private Long customFormId;

    /**
     * 所属对象
     */
    @ApiModelProperty(name="customFormCode",value = "所属对象")
    @Excel(name = "所属对象", height = 20, width = 30,orderNum="") 
    @Column(name = "custom_form_code")
    private String customFormCode;

    /**
     * 所属表单
     */
    @ApiModelProperty(name="customFormName",value = "所属表单")
    @Excel(name = "所属表单", height = 20, width = 30,orderNum="") 
    @Column(name = "custom_form_name")
    private String customFormName;

    /**
     * 关联子表单
     */
    @ApiModelProperty(name="subId",value = "关联子表单")
    @Excel(name = "关联子表单", height = 20, width = 30,orderNum="") 
    @Column(name = "sub_id")
    private Long subId;

    /**
     * 表单路由
     */
    @ApiModelProperty(name="fromRout",value = "表单路由")
    @Excel(name = "表单路由", height = 20, width = 30,orderNum="") 
    @Column(name = "from_rout")
    private String fromRout;

    /**
     * 表单列宽
     */
    @ApiModelProperty(name="itemWidth",value = "表单列宽")
    @Excel(name = "表单列宽", height = 20, width = 30,orderNum="") 
    @Column(name = "item_width")
    private String itemWidth;

    /**
     * 创建人id
     */
    @ApiModelProperty(name="createUserId",value = "创建人id")
    @Excel(name = "创建人id", height = 20, width = 30,orderNum="") 
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
     * 修改人id
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人id")
    @Excel(name = "修改人id", height = 20, width = 30,orderNum="") 
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
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="orgId",value = "组织ID")
    @Excel(name = "组织ID", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    private static final long serialVersionUID = 1L;
}