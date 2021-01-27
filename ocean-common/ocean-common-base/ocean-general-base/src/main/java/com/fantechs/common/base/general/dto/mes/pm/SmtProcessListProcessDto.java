package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/11/23
 */
@Data
public class SmtProcessListProcessDto extends SmtProcessListProcess implements Serializable {
    private static final long serialVersionUID = -8014622703067473837L;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 任务单号
     */
    @Transient
    @ApiModelProperty(name="taskCode",value = "任务单号")
    @Excel(name = "任务单号", height = 20, width = 30,orderNum="")
    private String taskCode;

    /**
     * 任务单号
     */
    @Transient
    @ApiModelProperty(name="barcodeTaskCode",value = "条码任务单号")
    @Excel(name = "条码任务单号", height = 20, width = 30,orderNum="")
    private String barcodeTaskCode;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    private String materialCode;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 工艺路线ID
     */
    @Transient
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
    /**
     * 流程单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCardId",value = "流程单号")
    private String workOrderCardId;

    /**
     * 生产数量
     */
    @Transient
    @ApiModelProperty(name = "productionQuantity",value = "生产数量")
    private BigDecimal productionQuantity;
    /**
     * 工单生产数量
     */
    @Transient
    @ApiModelProperty(name = "workOrderQuantity",value = "工单生产数量")
    private BigDecimal workOrderQuantity;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;
    /**
     * 包装单位-名称
     */
    @Transient
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    private String packingUnitName;
}
