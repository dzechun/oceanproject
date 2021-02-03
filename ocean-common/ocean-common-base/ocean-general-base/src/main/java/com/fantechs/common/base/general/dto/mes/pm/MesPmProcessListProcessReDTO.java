package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessListProcessRe;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesPmProcessListProcessReDTO extends MesPmProcessListProcessRe implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    private String modifiedUserName;
    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(value = "组织名称",example = "组织名称")
    private String organizationName;
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum = "0")
    private String workOrderCode;

    /**
     * 任务单号
     */
    @Transient
    @ApiModelProperty(name="taskCode",value = "任务单号")
    private String taskCode;

    /**
     * 任务单号
     */
    @Transient
    @ApiModelProperty(name="barcodeTaskCode",value = "条码任务单号")
    private String barcodeTaskCode;

    /**
     * 部件名称
     */
    @Transient
    @ApiModelProperty(name="partsInformationName" ,value="部件名称")
    @Excel(name = "部件名称", height = 20, width = 30,orderNum = "5")
    private String partsInformationName;

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
    @Excel(name = "物料料号", height = 20, width = 30,orderNum = "3")
    private String materialCode;

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
    @Excel(name = "工序名称", height = 20, width = 30,orderNum = "14")
    private String processName;
    /**
     * 退回工序名称
     */
    @Transient
    @ApiModelProperty(name="reProcessName" ,value="退回工序名称")
    @Excel(name = "退回工序名称", height = 20, width = 30,orderNum = "15")
    private String reProcessName;

    /**
     * 流程单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCardId",value = "流程单号")
    @Excel(name = "流程单号", height = 20, width = 30,orderNum = "1")
    private String workOrderCardId;
    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum = "4")
    private String materialDesc;
    /**
     * 投产数量
     */
    @Transient
    @ApiModelProperty(name = "productionQuantity",value = "投产数量")
    private BigDecimal productionQuantity;
    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name = "workOrderQuantity",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum = "10")
    private BigDecimal workOrderQuantity;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum = "6")
    private String productModuleName;
    /**
     * 包装单位-名称
     */
    @Transient
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    @Excel(name = "单位", height = 20, width = 30,orderNum = "7")
    private String packingUnitName;
    /**
     * 报工数量
     */
    @ApiModelProperty(name="outputQuantity",value = "报工数量")
    @Excel(name = "报工数量", height = 20, width = 30,orderNum = "11")
    private BigDecimal outputQuantity;
    /**
     * 员工名称
     */
    @Transient
    @ApiModelProperty(name = "staffName",value = "员工名称")
    @Excel(name = "员工名称", height = 20, width = 30,orderNum = "9")
    private String staffName;
    /**
     * 班组名称
     */
    @Transient
    @ApiModelProperty(name = "teamName",value = "班组名称")
    @Excel(name = "班组名称", height = 20, width = 30,orderNum = "8")
    private String teamName;
}