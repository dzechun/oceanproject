package com.fantechs.common.base.general.entity.ews;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.basic.search.QuartzSearch;
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
 * 程序排程
 * base_process_scheduling
 * @author mr.lei
 * @date 2021-03-08 17:27:49
 */
@Data
@Table(name = "ews_process_scheduling")
public class EwsProcessScheduling extends ValidGroup implements Serializable {
    /**
     * 排程id
     */
    @ApiModelProperty(name="processSchedulingId",value = "排程id")
    @Excel(name = "排程id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "process_scheduling_id")
    private Long processSchedulingId;

    /**
     * 排程名称
     */
    @ApiModelProperty(name="processSchedulingName",value = "排程名称")
    @Excel(name = "排程名称", height = 20, width = 30,orderNum="") 
    @Column(name = "process_scheduling_name")
    private String processSchedulingName;

    /**
     * 数据交互类型
     */
    @ApiModelProperty(name="dataInteractionType",value = "数据交互类型")
    @Excel(name = "数据交互类型", height = 20, width = 30,orderNum="") 
    @Column(name = "`data_interaction_type`")
    private Byte dataInteractionType;

    /**
     * 执行对象
     */
    @ApiModelProperty(name="executeObject",value = "执行对象")
    @Excel(name = "执行对象", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_object")
    private String executeObject;

    /**
     * 执行对象类别 0：WebService、1：动态库、2：exe、3：存储过程、4：脚本、5：文本
     */
    @ApiModelProperty(name="executeObjectType",value = "执行对象类别")
    @Excel(name = "执行对象类别", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_object_type")
    private Byte executeObjectType;

    /**
     * 执行规则
     */
    @ApiModelProperty(name="cron",value = "执行规则")
    @Excel(name = "执行规则", height = 20, width = 30,orderNum="") 
    private String cron;

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
     * 执行状态：0:未启动、1:运行中
     */
    @ApiModelProperty(name="executeStatus",value = "执行状态：0:未启动、1:运行中")
    @Excel(name = "执行状态：0:未启动、1:运行中", height = 20, width = 30,orderNum="") 
    @Column(name = "execute_status")
    private Byte executeStatus;

    /**
     * 读路径
     */
    @ApiModelProperty(name="readPath",value = "读路径")
    @Excel(name = "读路径", height = 20, width = 30,orderNum="")
    @Column(name = "read_path")
    private String readPath;

    /**
     * 写路径
     */
    @ApiModelProperty(name="writePath",value = "写路径")
    @Excel(name = "写路径", height = 20, width = 30,orderNum="")
    @Column(name = "write_path")
    private String writePath;


    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    @Column(name = "remark")
    private String remark;

    /**
     * 描述
     */
    @ApiModelProperty(name="processSchedulingDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30,orderNum="")
    @Column(name = "process_scheduling_desc")
    private String processSchedulingDesc;

    private QuartzSearch quartzSearch;

    private static final long serialVersionUID = 1L;
}