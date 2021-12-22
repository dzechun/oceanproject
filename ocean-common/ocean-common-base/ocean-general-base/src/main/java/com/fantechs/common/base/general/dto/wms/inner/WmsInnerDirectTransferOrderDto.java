package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WmsInnerDirectTransferOrderDto extends WmsInnerDirectTransferOrder implements Serializable {

    /**
     * 数量
     */
    @Transient
    @ApiModelProperty(name="actualQty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="3")
    private BigDecimal qty;

    /**
     * 作业人员
     */
    @ApiModelProperty(name="workerUserName",value = "作业人员")
    @Excel(name = "作业人员", height = 20, width = 30,orderNum = "4")
    private String workerUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum = "6")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum = "8")
    @Transient
    private String modifiedUserName;


    List<WmsInnerDirectTransferOrderDetDto> wmsInnerDirectTransferOrderDetDtos;

    List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeDtos;

    private static final long serialVersionUID = 1L;
}
