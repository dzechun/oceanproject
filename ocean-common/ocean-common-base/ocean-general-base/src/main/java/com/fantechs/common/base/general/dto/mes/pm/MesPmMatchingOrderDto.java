package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmMatchingOrderDto extends MesPmMatchingOrder implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 配套员名称
     */
    @ApiModelProperty(name="matchingStaffName",value = "配套员名称")
    @Transient
    @Excel(name = "配套人员", height = 20, width = 30)
    private String matchingStaffName;

    /**
     * 部门代码
     */
    @ApiModelProperty(name="deptCode" ,value="部门代码")
    @Transient
    private String deptCode;

    /**
     * 部门名称
     */
    @ApiModelProperty(name="deptName" ,value="部门名称")
    @Transient
    @Excel(name = "部门", height = 20, width = 30)
    private String deptName;

    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    @Transient
    @Excel(name = "流程单号", height = 20, width = 30)
    private String workOrderCardId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Transient
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Transient
    @Excel(name = "产品编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Transient
    @Excel(name = "产品名称", height = 20, width = 30)
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Transient
    @Excel(name = "产品描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Transient
    @Excel(name = "单位", height = 20, width = 30)
    private String mainUnit;

    /**
     * 产品型号名称
     */
    @ApiModelProperty(name="productModelName",value = "产品型号名称")
    @Transient
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelName;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Transient
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Transient
    private Long materialId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Transient
    private Long storageId;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode",value = "储位编码")
    @Transient
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName",value = "储位名称")
    @Transient
    private String storageName;

    /**
     * 最小齐套数
     */
    @ApiModelProperty(name="minMatchingQuantity",value = "最小齐套数")
    @Transient
    private BigDecimal minMatchingQuantity;

    /**
     * 已配套数
     */
    @ApiModelProperty(name="alreadyMatchingQuantity",value = "已配套数")
    @Transient
    private BigDecimal alreadyMatchingQuantity;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQuantity",value = "工单数量")
    @Transient
    private BigDecimal workOrderQuantity;

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQuantity",value = "投产数量")
    @Transient
    private BigDecimal productionQuantity;
}
