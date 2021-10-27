package com.fantechs.common.base.general.dto.leisai.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class LeisaiProcessInputOrderImport implements Serializable {

    /**
     * 单据号
     */
    @ApiModelProperty(name="processInputOrderCode",value = "单据号")
    @Excel(name = "单据号", height = 20, width = 30)
    private String processInputOrderCode;

    /**
     * 完成日期
     */
    @ApiModelProperty(name="finishedTime",value = "完成日期")
    @Excel(name = "完成日期", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date finishedTime;

    /**
     * 生产订单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产订单")
    @Excel(name = "生产订单", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30)
    private BigDecimal orderQty;

    /**
     * 工序号
     */
    @ApiModelProperty(name="processCode",value = "工序号")
    @Excel(name = "工序号", height = 20, width = 30)
    private String processCode;

    /**
     * 工序短文本
     */
    @ApiModelProperty(name="processShortText",value = "工序短文本")
    @Excel(name = "工序短文本", height = 20, width = 30)
    private String processShortText;

    /**
     * 投入数
     */
    @ApiModelProperty(name="putIntoQty",value = "投入数")
    @Excel(name = "投入数", height = 20, width = 30)
    private BigDecimal putIntoQty;

    /**
     * 不良数
     */
    @ApiModelProperty(name="badnessQty",value = "不良数")
    @Excel(name = "不良数", height = 20, width = 30)
    private BigDecimal badnessQty;

    /**
     * 测试人数
     */
    @ApiModelProperty(name="testUserCount",value = "测试人数")
    @Excel(name = "测试人数", height = 20, width = 30)
    private BigDecimal testUserCount;

    /**
     * 使用时间
     */
    @ApiModelProperty(name="hoursOfUse",value = "使用时间")
    @Excel(name = "使用时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm")
    @JSONField(format ="yyyy-MM-dd HH:mm")
    private Date hoursOfUse;

    /**
     * 测试方式
     */
    @ApiModelProperty(name="testType",value = "测试方式")
    @Excel(name = "测试方式", height = 20, width = 30)
    private String testType;

    /**
     * 记录人
     */
    @ApiModelProperty(name="recorder",value = "记录人")
    @Excel(name = "记录人", height = 20, width = 30)
    private String recorder;

    /**
     * 人数备注
     */
    @ApiModelProperty(name="userCountRemark",value = "人数备注")
    @Excel(name = "人数备注", height = 20, width = 30)
    private String userCountRemark;


    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;


}
