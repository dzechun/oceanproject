package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WmsInnerJobOrderDto extends WmsInnerJobOrder implements Serializable {

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 单据类型
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据类型")
    private String orderTypeName;

    /**
     * 核心单据类型
     */
    @Transient
    @ApiModelProperty(name = "coreOrderTypeName",value = "核心单据类型")
    private String coreOrderTypeName;

    /**
     * 工作人员
     */
    @Transient
    @ApiModelProperty(name = "workName",value = "工作人员")
    private String workName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 计划数量
     */
    @Transient
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="")
    private BigDecimal planQty;

    /**
     * 拣货数量
     */
    @Transient
    @ApiModelProperty(name="actualQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="")
    private BigDecimal actualQty;

    /**
     * 分配数量
     */
    @Transient
    @ApiModelProperty(name="distributionQty",value = "分配数量")
    @Excel(name = "分配数量", height = 20, width = 30,orderNum="")
    private BigDecimal distributionQty;

    List<WmsInnerMaterialBarcodeReOrderDto> wmsInnerMaterialBarcodeReOrderDtos;

}
