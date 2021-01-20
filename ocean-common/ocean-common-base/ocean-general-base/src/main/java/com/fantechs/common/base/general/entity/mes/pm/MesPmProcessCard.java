package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 工序流转卡
 * mes_pm_process_card
 * @author mr.lei
 * @date 2021-01-19 19:12:34
 */
@Data
@Table(name = "mes_pm_process_card")
public class MesPmProcessCard extends ValidGroup implements Serializable {
    /**
     * 工序流转卡id
     */
    @ApiModelProperty(name="processCardId",value = "工序流转卡id")
    @Excel(name = "工序流转卡id", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "process_card_id")
    private Long processCardId;

    /**
     * 工单id
     */
    @ApiModelProperty(name="workOrderId",value = "工单id")
    @Excel(name = "工单id", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    @Excel(name = "工单流转卡编码", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_card_id")
    private String workOrderCardId;

    /**
     * 流转卡类型：1：工序过站、2:工序过站物料明细、3:完工上报、4:不良品、5:绑定关键物料、6:拆批作业、7:合批作业
     */
    @ApiModelProperty(name="processCardType",value = "流转卡类型：1：工序过站、2:工序过站物料明细、3:完工上报、4:不良品、5:绑定关键物料、6:拆批作业、7:合批作业")
    @Excel(name = "流转卡类型：1：工序过站、2:工序过站物料明细、3:完工上报、4:不良品、5:绑定关键物料、6:拆批作业、7:合批作业", height = 20, width = 30,orderNum="")
    @Column(name = "process_card_type")
    private Byte processCardType;

    /**
     * 绑定id
     */
    @ApiModelProperty(name="boundId",value = "绑定id")
    @Excel(name = "绑定id", height = 20, width = 30,orderNum="")
    @Column(name = "bound_id")
    private Long boundId;

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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="")
    private String option3;

    private static final long serialVersionUID = 1L;
}