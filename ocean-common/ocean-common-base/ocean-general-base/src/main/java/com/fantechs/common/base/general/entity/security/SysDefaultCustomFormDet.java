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
 * 自定义表单明细
 * sys_default_custom_form_det
 * @author admin
 * @date 2021-08-27 11:48:44
 */
@Data
@Table(name = "sys_default_custom_form_det")
public class SysDefaultCustomFormDet extends ValidGroup implements Serializable {
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
    @ApiModelProperty(name="itemKey",value = "字段名")
    @Excel(name = "字段名", height = 20, width = 30,orderNum="")
    @Column(name = "item_key")
    private String itemKey;

    /**
     * 字段中文名
     */
    @ApiModelProperty(name="itemName",value = "字段中文名")
    @Excel(name = "字段中文名", height = 20, width = 30,orderNum="")
    @Column(name = "item_name")
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
    @ApiModelProperty(name="itemValue",value = "搜索用默认值")
    @Excel(name = "搜索用默认值", height = 20, width = 30,orderNum="")
    @Column(name = "item_value")
    private String itemValue;

    /**
     * 列宽
     */
    @ApiModelProperty(name="itemWidth",value = "列宽")
    @Excel(name = "列宽", height = 20, width = 30,orderNum="")
    @Column(name = "item_width")
    private String itemWidth;

    /**
     * 下拉自定义名称
     */
    @ApiModelProperty(name="selectName",value = "下拉自定义名称")
    @Excel(name = "下拉自定义名称", height = 20, width = 30,orderNum="")
    @Column(name = "select_name")
    private String selectName;

    /**
     * 下拉自定义值
     */
    @ApiModelProperty(name="selectValue",value = "下拉自定义值")
    @Excel(name = "下拉自定义值", height = 20, width = 30,orderNum="")
    @Column(name = "select_value")
    private String selectValue;

    /**
     * 是否编辑禁用（0-默认 1-禁用）
     */
    @ApiModelProperty(name="disabled",value = "是否编辑禁用（0-默认 1-禁用）")
    @Excel(name = "是否编辑禁用（0-默认 1-禁用）", height = 20, width = 30,orderNum="")
    private Byte disabled;

    /**
     * 列表字段属性（0-默认 1-input 2-select  3-switch 4-data）
     */
    @ApiModelProperty(name="itemListType",value = "列表字段属性（0-默认 1-input 2-select  3-switch 4-data）")
    @Excel(name = "列表字段属性（0-默认 1-input 2-select  3-switch 4-data）", height = 20, width = 30,orderNum="")
    @Column(name = "item_list_type")
    private Byte itemListType;

    /**
     * 检索字段属性（0-默认 1-input 2-select  3-switch 4-data）
     */
    @ApiModelProperty(name="itemSearchType",value = "检索字段属性（0-默认 1-input 2-select  3-switch 4-data）")
    @Excel(name = "检索字段属性（0-默认 1-input 2-select  3-switch 4-data）", height = 20, width = 30,orderNum="")
    @Column(name = "item_search_type")
    private Byte itemSearchType;

    /**
     * 详情字段属性（0-默认 1-input 2-select  3-switch 4-data）
     */
    @ApiModelProperty(name="itemDetailType",value = "详情字段属性（0-默认 1-input 2-select  3-switch 4-data）")
    @Excel(name = "详情字段属性（0-默认 1-input 2-select  3-switch 4-data）", height = 20, width = 30,orderNum="")
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
     * 是否编辑展示(0-否 1-是)
     */
    @ApiModelProperty(name="isEditDisplay",value = "是否编辑展示(0-否 1-是)")
    @Excel(name = "是否编辑展示(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_edit_display")
    private Byte isEditDisplay;

    /**
     * 配置项Code
     */
    @ApiModelProperty(name="specCode",value = "配置项Code")
    @Excel(name = "配置项Code", height = 20, width = 30,orderNum="")
    @Column(name = "spec_code")
    private String specCode;

    /**
     * 事件触发方式
     */
    @ApiModelProperty(name="eventTriggerMode",value = "事件触发方式")
    @Excel(name = "事件触发方式", height = 20, width = 30,orderNum="")
    @Column(name = "event_trigger_mode")
    private Byte eventTriggerMode;

    /**
     * 事件触发名称
     */
    @ApiModelProperty(name="eventTriggerName",value = "事件触发名称")
    @Excel(name = "事件触发名称", height = 20, width = 30,orderNum="")
    @Column(name = "event_trigger_name")
    private Byte eventTriggerName;

    /**
     * 枚举json
     */
    @ApiModelProperty(name="enumData",value = "枚举json")
    @Excel(name = "枚举json", height = 20, width = 30,orderNum="")
    @Column(name = "enum_data")
    private String enumData;

    /**
     * 是否必填（0-否 1-是）
     */
    @ApiModelProperty(name="isRequired",value = "是否必填（0-否 1-是）")
    @Excel(name = "是否必填（0-否 1-是）", height = 20, width = 30,orderNum="")
    @Column(name = "is_required")
    private Byte isRequired;

    /**
     * 时间显示格式
     */
    @ApiModelProperty(name="format",value = "时间显示格式")
    @Excel(name = "时间显示格式", height = 20, width = 30,orderNum="")
    private String format;

    /**
     * 后台所需时间格式
     */
    @ApiModelProperty(name="valueFormat",value = "后台所需时间格式")
    @Excel(name = "后台所需时间格式", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "value_format")
    private String valueFormat;

    /**
     * 时间类型
     */
    @ApiModelProperty(name="dateType",value = "时间类型")
    @Excel(name = "时间类型", height = 20, width = 30,orderNum="")
    @Column(name = "date_type")
    private String dateType;

    /**
     * 排序
     */
    @ApiModelProperty(name="orderNum",value = "排序")
    @Excel(name = "排序", height = 20, width = 30,orderNum="")
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 是否检索(0-否 1-是)
     */
    @ApiModelProperty(name="isSearch",value = "是否检索(0-否 1-是)")
    @Excel(name = "是否检索(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_search")
    private Byte isSearch;

    /**
     * 是否表格(0-否 1-是)
     */
    @ApiModelProperty(name="isTable",value = "是否表格(0-否 1-是)")
    @Excel(name = "是否表格(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_table")
    private Byte isTable;

    /**
     * 是否表单(0-否 1-是)
     */
    @ApiModelProperty(name="isForm",value = "是否表单(0-否 1-是)")
    @Excel(name = "是否表单(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_form")
    private Byte isForm;

    /**
     * 是否详情(0-否 1-是)
     */
    @ApiModelProperty(name="isDetail",value = "是否详情(0-否 1-是)")
    @Excel(name = "是否详情(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "is_detail")
    private Byte isDetail;

    /**
     * 排序
     */
    @ApiModelProperty(name="querySeq",value = "排序")
    @Excel(name = "排序", height = 20, width = 30)
    @Column(name = "query_seq")
    private String querySeq;

    /**
     * 查询方式
     */
    @ApiModelProperty(name="queryType",value = "查询方式")
    @Excel(name = "查询方式", height = 20, width = 30)
    @Column(name = "query_type")
    private String queryType;

    /**
     * 连接查询
     */
    @ApiModelProperty(name="joinQuery",value = "连接查询")
    @Excel(name = "连接查询", height = 20, width = 30)
    @Column(name = "join_query")
    private String joinQuery;

    /**
     * 是否高级查询
     */
    @ApiModelProperty(name="isHighGrade",value = "是否高级查询")
    @Excel(name = "是否高级查询", height = 20, width = 30)
    @Column(name = "is_high_grade")
    private String isHighGrade;

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

    @ApiModelProperty(name="minValue",value = "最小值")
    @Excel(name = "最小值", height = 20, width = 30)
    @Column(name = "min_value")
    private String minValue;

    @ApiModelProperty(name="maxValue",value = "最大值")
    @Excel(name = "最大值", height = 20, width = 30)
    @Column(name = "max_value")
    private String maxValue;

    private static final long serialVersionUID = 1L;
}
