package com.fantechs.common.base.general.dto.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WmsInInPlanOrderDto extends WmsInInPlanOrder implements Serializable {

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="4")
    private String warehouseName;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name="storageName",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="5")
    private String storageName;

    /**
     * 计划总数量
     */
    @ApiModelProperty(name="planQty",value = "计划总数量")
    @Excel(name = "计划总数量", height = 20, width = 30,orderNum="")
    private BigDecimal totalPlanQty;

    /**
     * 上架总数量
     */
    @ApiModelProperty(name="putawayQty",value = "上架总数量")
    @Excel(name = "上架总数量", height = 20, width = 30,orderNum="")
    private BigDecimal totalPutawayQty;

    /**
     * 制单人
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人")
    @Excel(name = "制单人", height = 20, width = 30,orderNum="")
    private String makeOrderUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    @Excel(name = "组织", height = 20, width = 30,orderNum="8")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="19")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="21")
    private String modifiedUserName;

    List<WmsInInPlanOrderDetDto> wmsInInPlanOrderDetDtos;

}
