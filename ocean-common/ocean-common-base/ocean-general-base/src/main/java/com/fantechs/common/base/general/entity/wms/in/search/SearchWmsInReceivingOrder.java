package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/14
 */
@Data
public class SearchWmsInReceivingOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    @ApiModelProperty(name = "sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    @ApiModelProperty(name = "sysOrderTypeCode",value = "系统单据类型编码")
    private String sysOrderTypeCode;

    @ApiModelProperty(name = "receivingOrderCode",value = "收货单号")
    private String receivingOrderCode;

    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(name = "orderStatusList",value = "单据状态集合")
    private List<Byte> orderStatusList;
}
