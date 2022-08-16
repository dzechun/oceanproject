package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EamJigRequisitionWorkOrderDto implements Serializable {

    /**
     * 工单ID
     */
    @Transient
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="5")
    private String workOrderCode;

    /**
     * 产品料号ID
     */
    @Transient
    @ApiModelProperty(name="materialId",value = "产品料号ID")
    private Long materialId;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 料号描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "料号描述")
    private String materialDesc;

    /**
     * 生产线名
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "生产线名")
    private String proName;

    /**
     * 治具绑定产品
     */
    @ApiModelProperty(name="list",value = "治具绑定产品")
    private List<EamJigMaterialDto> list= new ArrayList<>();

}
