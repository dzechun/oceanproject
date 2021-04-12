package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 工单流转卡配置表
 * base_work_order_card_collocation
 * @author 18358
 * @date 2020-11-20 15:24:21
 */
@Data
@Table(name = "base_work_order_card_collocation")
public class SmtWorkOrderCardCollocation extends ValidGroup implements Serializable {
    /**
     * 工单流转卡配置ID
     */
    @ApiModelProperty(name="workOrderCardCollocationId",value = "工单流转卡配置ID")
    @Excel(name = "工单流转卡配置ID", height = 20, width = 30)
    @Id
    @Column(name = "work_order_card_collocation_id")
    private Long workOrderCardCollocationId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30)
    @Column(name = "work_order_id")
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    /**
     * 产生数量
     */
    @ApiModelProperty(name="produceQuantity",value = "产生数量")
    @Excel(name = "产生数量", height = 20, width = 30)
    @Column(name = "produce_quantity")
    @NotNull(message = "产生数量不能为空")
    private Integer produceQuantity;

    /**
     * 已产生数量
     */
    @ApiModelProperty(name="generatedQuantity",value = "已产生数量")
    @Excel(name = "已产生数量", height = 20, width = 30)
    @Column(name = "generated_quantity")
    private Integer generatedQuantity;

    /**
     * 打印ID
     */
    @ApiModelProperty(name="printId",value = "打印ID")
    @Excel(name = "打印ID", height = 20, width = 30)
    @Column(name = "print_id")
    private Long printId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、未生成 1、部分生成 2、已生成）
     */
    @ApiModelProperty(name="status",value = "状态（0、未生成 1、部分生成 2、已生成）")
    @Excel(name = "状态（0、未生成 1、部分生成 2、已生成）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

    private static final long serialVersionUID = 1L;
}
