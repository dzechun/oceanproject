package com.fantechs.common.base.general.entity.leisai.history;

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
 * 制程数据录入单履历表
 * leisai_ht_process_input_order
 * @author 81947
 * @date 2021-10-26 17:41:07
 */
@Data
@Table(name = "leisai_ht_process_input_order")
public class LeisaiHtProcessInputOrder extends ValidGroup implements Serializable {
    /**
     * 制程数据录入单履历ID
     */
    @ApiModelProperty(name="htProcessInputOrderId",value = "制程数据录入单履历ID")
    @Excel(name = "制程数据录入单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_process_input_order_id")
    private Long htProcessInputOrderId;

    /**
     * 制程数据录入单ID
     */
    @ApiModelProperty(name="processInputOrderId",value = "制程数据录入单ID")
    @Excel(name = "制程数据录入单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_input_order_id")
    private Long processInputOrderId;

    /**
     * 单据号
     */
    @ApiModelProperty(name="processInputOrderCode",value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30,orderNum="") 
    @Column(name = "process_input_order_code")
    private String processInputOrderCode;

    /**
     * 完成日期
     */
    @ApiModelProperty(name="finishedTime",value = "完成日期")
    @Excel(name = "完成日期", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_time")
    private Date finishedTime;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    @Excel(name = "生产订单", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="") 
    @Column(name = "material_desc")
    private String materialDesc;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="") 
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 工序号
     */
    @ApiModelProperty(name="processCode",value = "工序号")
    @Excel(name = "工序号", height = 20, width = 30,orderNum="") 
    @Column(name = "process_code")
    private String processCode;

    /**
     * 工序短文本
     */
    @ApiModelProperty(name="processShortText",value = "工序短文本")
    @Excel(name = "工序短文本", height = 20, width = 30,orderNum="") 
    @Column(name = "process_short_text")
    private String processShortText;

    /**
     * 投入数
     */
    @ApiModelProperty(name="putIntoQty",value = "投入数")
    @Excel(name = "投入数", height = 20, width = 30,orderNum="") 
    @Column(name = "put_into_qty")
    private BigDecimal putIntoQty;

    /**
     * 不良数
     */
    @ApiModelProperty(name="badnessQty",value = "不良数")
    @Excel(name = "不良数", height = 20, width = 30,orderNum="") 
    @Column(name = "badness_qty")
    private BigDecimal badnessQty;

    /**
     * 测试人数
     */
    @ApiModelProperty(name="testUserCount",value = "测试人数")
    @Excel(name = "测试人数", height = 20, width = 30,orderNum="") 
    @Column(name = "test_user_count")
    private BigDecimal testUserCount;

    /**
     * 使用时间
     */
    @ApiModelProperty(name="hoursOfUse",value = "使用时间")
    @Excel(name = "使用时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "hours_of_use")
    private Date hoursOfUse;

    /**
     * 测试方式
     */
    @ApiModelProperty(name="testType",value = "测试方式")
    @Excel(name = "测试方式", height = 20, width = 30,orderNum="") 
    @Column(name = "test_type")
    private String testType;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    @Excel(name = "记录人", height = 20, width = 30,orderNum="") 
    private String recorder;

    /**
     * 人数备注
     */
    @ApiModelProperty(name="userCountRemark",value = "人数备注")
    @Excel(name = "人数备注", height = 20, width = 30,orderNum="") 
    @Column(name = "user_count_remark")
    private String userCountRemark;

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

    private static final long serialVersionUID = 1L;
}