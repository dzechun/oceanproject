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

;

/**
 * 自定义表单明细
 * sys_custom_form_det
 * @author lfz
 * @date 2021-01-08 20:48:14
 */
@Data
@Table(name = "sys_custom_form_det")
public class SysCustomFormDet extends ValidGroup implements Serializable {
    /**
     * 自定义表单明细Id
     */
    @ApiModelProperty(name="customFormDetId",value = "自定义表单明细Id")
    @Excel(name = "自定义表单明细Id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "custom_form_det_id")
    private Long customFormDetId;

    /**
     * 自定义表单Id
     */
    @ApiModelProperty(name="customFormId",value = "自定义表单Id")
    @Excel(name = "自定义表单Id", height = 20, width = 30,orderNum="")
    @Column(name = "custom_form_id")
    private Long customFormId;

    /**
     * 字段名
     */
    @ApiModelProperty(name="item_key",value = "字段名")
    @Excel(name = "字段名", height = 20, width = 30,orderNum="") 
    private String itemKey;

    /**
     * 字段中文名
     */
    @ApiModelProperty(name="item_name",value = "字段中文名")
    @Excel(name = "字段中文名", height = 20, width = 30,orderNum="") 
    private String itemName;

    /**
     * 显示类型（0-全部 1-检索条件 2-列表页 3-详情页 4-检索加列表 5-列表加详情）
     */
    @ApiModelProperty(name="showType",value = "显示类型（0-全部 1-检索条件 2-列表页 3-详情页 4-检索加列表 5-列表加详情）")
    @Excel(name = "显示类型（0-全部 1-检索条件 2-列表页 3-详情页 4-检索加列表 5-列表加详情）", height = 20, width = 30,orderNum="") 
    @Column(name = "show_type")
    private Byte showType;

    /**
     * 搜索用默认值
     */
    @ApiModelProperty(name="item_value",value = "搜索用默认值")
    @Excel(name = "搜索用默认值", height = 20, width = 30,orderNum="") 
    private String itemValue;

    /**
     * 列表字段属性（0-input 1-select  2-switch 3-data）
     */
    @ApiModelProperty(name="itemListType",value = "列表字段属性（0-input 1-select  2-switch 3-data）")
    @Excel(name = "列表字段属性（0-input 1-select  2-switch 3-data）", height = 20, width = 30,orderNum="") 
    @Column(name = "item_list_type")
    private Byte itemListType;

    /**
     * 检索字段属性（0-input 1-select  2-switch 3-data）
     */
    @ApiModelProperty(name="itemSearchType",value = "检索字段属性（0-input 1-select  2-switch 3-data）")
    @Excel(name = "检索字段属性（0-input 1-select  2-switch 3-data）", height = 20, width = 30,orderNum="") 
    @Column(name = "item_search_type")
    private Byte itemSearchType;

    /**
     * 详情字段属性（0-input 1-select  2-switch 3-data）
     */
    @ApiModelProperty(name="itemDetailType",value = "详情字段属性（0-input 1-select  2-switch 3-data）")
    @Excel(name = "详情字段属性（0-input 1-select  2-switch 3-data）", height = 20, width = 30,orderNum="") 
    @Column(name = "item_detail_type")
    private Byte itemDetailType;

    /**
     * 是否隐藏（0-否 1-是）
     */
    @ApiModelProperty(name="isHide",value = "是否隐藏（0-否 1-是）")
    @Excel(name = "是否隐藏（0-否 1-是）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_hide")
    private Byte isHide;

    /**
     * 是否枚举（0-否 1-是）
     */
    @ApiModelProperty(name="isEnum",value = "是否枚举（0-否 1-是）")
    @Excel(name = "是否枚举（0-否 1-是）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_enum")
    private Byte isEnum;

    /**
     * 是否必填（0-否 1-是）
     */
    @ApiModelProperty(name="isRequired",value = "是否必填（0-否 1-是）")
    @Excel(name = "是否必填（0-否 1-是）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_required")
    private Byte isRequired;

    /**
     * 排序
     */
    @ApiModelProperty(name="orderNum",value = "排序")
    @Excel(name = "排序", height = 20, width = 30,orderNum="") 
    @Column(name = "order_num")
    private Integer orderNum;

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
     * 角色状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "角色状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}