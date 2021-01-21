package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MesPmMatchingOrderDto extends MesPmMatchingOrder implements Serializable {

    /**
     * 员工编码
     */
    @ApiModelProperty(name="staffCode",value = "员工编码")
    @Transient
    private String staffCode;

    /**
     * 员工名称
     */
    @ApiModelProperty(name="staffName",value = "员工名称")
    @Transient
    private String staffName;

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
    private String deptName;

    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    @Transient
    private String workOrderCardId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Transient
    private String workOrderCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Transient
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Transient
    private String materialDesc;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Transient
    private String mainUnit;

    /**
     * 产品型号名称
     */
    @ApiModelProperty(name="productModelName",value = "产品型号名称")
    @Transient
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
}
