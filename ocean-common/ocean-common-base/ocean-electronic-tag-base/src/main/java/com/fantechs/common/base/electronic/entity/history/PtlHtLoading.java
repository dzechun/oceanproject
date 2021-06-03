package com.fantechs.common.base.electronic.entity.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 上料单履历表
 * smt_ht_loading
 * @author 86178
 * @date 2021-01-09 16:29:47
 */
@Data
@Table(name = "smt_ht_loading")
public class PtlHtLoading implements Serializable {
    /**
     * 上料单履历表Id
     */
    @ApiModelProperty(name="htLoadingId",value = "上料单履历表Id")
    @Excel(name = "上料单履历表Id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_loading_id")
    private Long htLoadingId;

    /**
     * 上料单Id
     */
    @ApiModelProperty(name="loadingId",value = "上料单Id")
    @Excel(name = "上料单Id", height = 20, width = 30,orderNum="") 
    @Column(name = "loading_id")
    private Long loadingId;

    /**
     * 上料单号
     */
    @ApiModelProperty(name="loadingCode",value = "上料单号")
    @Excel(name = "上料单号", height = 20, width = 30,orderNum="") 
    @Column(name = "loading_code")
    private String loadingCode;

    /**
     * 来源系统名称
     */
    @ApiModelProperty(name="sourceSys",value = "来源系统名称")
    @Excel(name = "来源系统名称", height = 20, width = 30,orderNum="") 
    @Column(name = "source_sys")
    private String sourceSys;

    /**
     * 来源系统行Id
     */
    @ApiModelProperty(name="sourceSysId",value = "来源系统行Id")
    @Excel(name = "来源系统行Id", height = 20, width = 30,orderNum="") 
    @Column(name = "source_sys_id")
    private String sourceSysId;

    /**
     * 单据类型（1-采购单 2-入库单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-采购单 2-入库单）")
    @Excel(name = "单据类型（1-采购单 2-入库单）", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type")
    private Byte orderType;

    /**
     * 状态（0-未开始 1-上料中 2-部分完成 3-已完成）
     */
    @ApiModelProperty(name="status",value = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）")
    @Excel(name = "状态（0-未开始 1-上料中 2-部分完成 3-已完成）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 创建人Id
     */
    @ApiModelProperty(name="createUserId",value = "创建人Id")
    @Excel(name = "创建人Id", height = 20, width = 30,orderNum="") 
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
     * 逻辑删除（0-删除 1-正常）
     */
    @ApiModelProperty(name="isDetele",value = "逻辑删除（0-删除 1-正常）")
    @Excel(name = "逻辑删除（0-删除 1-正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_detele")
    private Byte isDetele;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织Id
     */
    @ApiModelProperty(name="orgId",value = "组织Id")
    @Excel(name = "组织Id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    private static final long serialVersionUID = 1L;
}