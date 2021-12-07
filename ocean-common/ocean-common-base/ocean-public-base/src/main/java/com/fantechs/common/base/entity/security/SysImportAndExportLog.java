package com.fantechs.common.base.entity.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 导入导出日志
 * sys_import_and_export_log
 * @author 81947
 * @date 2021-12-07 09:25:30
 */
@Data
@Table(name = "sys_import_and_export_log")
public class SysImportAndExportLog extends ValidGroup implements Serializable {
    /**
     * 导入导出日志ID
     */
    @ApiModelProperty(name="importAndExportLogId",value = "导入导出日志ID")
    @Excel(name = "导入导出日志ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "import_and_export_log_id")
    private Long importAndExportLogId;

    /**
     * 模块名
     */
    @ApiModelProperty(name="moduleNames",value = "模块名")
    @Excel(name = "模块名", height = 20, width = 30,orderNum="") 
    @Column(name = "module_names")
    private String moduleNames;

    /**
     * 文件名
     */
    @ApiModelProperty(name="fileName",value = "文件名")
    @Excel(name = "文件名", height = 20, width = 30,orderNum="") 
    @Column(name = "file_name")
    private String fileName;

    /**
     * 类型(1-导入EXCEL 2-导出EXCEL)
     */
    @ApiModelProperty(name="type",value = "类型(1-导入EXCEL 2-导出EXCEL)")
    @Excel(name = "类型(1-导入EXCEL 2-导出EXCEL)", height = 20, width = 30,orderNum="") 
    private Byte type;

    /**
     * 结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="result",value = "结果(0-失败 1-成功)")
    @Excel(name = "结果(0-失败 1-成功)", height = 20, width = 30,orderNum="") 
    private Byte result;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作用户ID")
    @Excel(name = "操作用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 总数量
     */
    @ApiModelProperty(name="totalCount",value = "总数量")
    @Excel(name = "总数量", height = 20, width = 30,orderNum="") 
    @Column(name = "total_count")
    private Integer totalCount;

    /**
     * 成功数量
     */
    @ApiModelProperty(name="succeedCount",value = "成功数量")
    @Excel(name = "成功数量", height = 20, width = 30,orderNum="") 
    @Column(name = "succeed_count")
    private Integer succeedCount;

    /**
     * 失败数量
     */
    @ApiModelProperty(name="failCount",value = "失败数量")
    @Excel(name = "失败数量", height = 20, width = 30,orderNum="") 
    @Column(name = "fail_count")
    private Integer failCount;

    /**
     * 耗时
     */
    @ApiModelProperty(name="consumeTime",value = "耗时")
    @Excel(name = "耗时", height = 20, width = 30,orderNum="") 
    @Column(name = "consume_time")
    private BigDecimal consumeTime;

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
     * 调用时间
     */
    @ApiModelProperty(name="requestTime",value = "调用时间")
    @Excel(name = "调用时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "request_time")
    private Date requestTime;

    /**
     * 响应时间
     */
    @ApiModelProperty(name="responseTime",value = "响应时间")
    @Excel(name = "响应时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "response_time")
    private Date responseTime;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 成功行号(建议用逗号隔开)
     */
    @ApiModelProperty(name="succeedInfo",value = "成功行号(建议用逗号隔开)")
    @Excel(name = "成功行号(建议用逗号隔开)", height = 20, width = 30,orderNum="") 
    @Column(name = "succeed_info")
    private String succeedInfo;

    /**
     * 失败信息(如果数量巨大，建议只存100条失败原因，或者不同类型的原因分别告知有多少行)
     */
    @ApiModelProperty(name="failInfo",value = "失败信息(如果数量巨大，建议只存100条失败原因，或者不同类型的原因分别告知有多少行)")
    @Excel(name = "失败信息(如果数量巨大，建议只存100条失败原因，或者不同类型的原因分别告知有多少行)", height = 20, width = 30,orderNum="") 
    @Column(name = "fail_info")
    private String failInfo;

    private static final long serialVersionUID = 1L;
}