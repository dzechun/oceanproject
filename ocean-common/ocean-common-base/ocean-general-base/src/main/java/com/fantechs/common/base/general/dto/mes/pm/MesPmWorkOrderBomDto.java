package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesPmWorkOrderBomDto extends MesPmWorkOrderBom implements Serializable {
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Transient
    @ApiModelProperty(name="workOrderType",value = "工单类型(0、量产 1、试产 2、返工 3、维修)")
    private Byte workOrderType;

    /**
     * 计划开始时间
     */
    @Transient
    @ApiModelProperty(name="planStartTime",value = "计划开始时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty" ,value="工单数量")
    private BigDecimal workOrderQty;

    /**
     * 产线ID
     */
    @Transient
    @ApiModelProperty(name="proLineId" ,value="产线ID")
    private Long proLineId;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="产线名称")
    private String proName;

    /**
     * 零件料号
     */
    @Transient
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    @Excel(name = "零件料号", height = 20, width = 30,orderNum="2")
    private String partMaterialCode;

    /**
     * 零件料号名称
     */
    @Transient
    @ApiModelProperty(name="partMaterialName" ,value="零件料号名称")
    private String partMaterialName;

    /**
     * 零件料号版本
     */
    @Transient
    @ApiModelProperty(name="partMaterialVersion" ,value="零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30,orderNum="3")
    private String partMaterialVersion;

    /**
     * 零件料号描述
     */
    @Transient
    @ApiModelProperty(name="partMaterialDesc" ,value="零件料号描述")
    @Excel(name = "零件料号描述", height = 20, width = 30,orderNum="4")
    private String partMaterialDesc;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    /**
     * 产品料号名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="产品料号名称")
    private String materialName;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="产品版本")
    private String materialVersion;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="产品描述")
    private String materialDesc;

    /**
     * 零件替代料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode" ,value="零件替代料号")
    private String subMaterialCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="8")
    private String processName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="10")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="12")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    @Transient
    @ApiModelProperty("下发数量")
    private BigDecimal issueQty;

    @Transient
    @ApiModelProperty("仓库ID")
    private Long warehouseId;
}
