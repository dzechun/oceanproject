package com.fantechs.common.base.general.entity.log;

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
 * 员工操作日志表
 * base_emp_operation_log
 *
 * @author jbb
 * @date 2021-03-12 18:45:47
 */
@Data
@Table(name = "base_emp_operation_log")
public class SmtEmpOperationLog extends ValidGroup implements Serializable {
    /**
     * 员工操作日志ID
     */
    @ApiModelProperty(name = "empOperationLogId", value = "员工操作日志ID")
    @Excel(name = "员工操作日志ID", height = 20, width = 30, orderNum = "")
    @Id
    @Column(name = "emp_operation_log_id")
    private Long empOperationLogId;

    /**
     * 单据号
     */
    @ApiModelProperty(name = "documentCode", value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30, orderNum = "")
    @Column(name = "document_code")
    private String documentCode;

    /**
     * 功能菜单
     */
    @ApiModelProperty(name = "functionMenu", value = "功能菜单")
    @Excel(name = "功能菜单", height = 20, width = 30, orderNum = "")
    @Column(name = "function_menu")
    private String functionMenu;

    /**
     * 主机
     */
    @ApiModelProperty(name = "host", value = "主机")
    @Excel(name = "主机", height = 20, width = 30, orderNum = "")
    private String host;

    /**
     * 操作类型
     */
    @ApiModelProperty(name = "operationType", value = "操作类型")
    @Excel(name = "操作类型", height = 20, width = 30, orderNum = "")
    @Column(name = "operation_type")
    private String operationType;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @Excel(name = "备注", height = 20, width = 30, orderNum = "")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name = "status", value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30, orderNum = "")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name = "organizationId", value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30, orderNum = "")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name = "createUserId", value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30, orderNum = "")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30, orderNum = "", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name = "modifiedUserId", value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30, orderNum = "")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "modifiedTime", value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30, orderNum = "", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name = "isDelete", value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30, orderNum = "")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
