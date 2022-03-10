package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchWmsInAsnOrder extends BaseQuery implements Serializable {
    /**
     * 货主信息
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主信息")
    private String materialOwnerName;

    /**
     * ASN单号
     */
    @ApiModelProperty(name="asnCode",value = "ASN单号")
    private String asnCode;

    /**
     * 订单号
     */
    @ApiModelProperty(name="sourceOrderCode",value = "订单号")
    private String sourceOrderCode;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 单据状态(1-待收货 2-收货中 3-收货完成)
     */
    @ApiModelProperty(name="orderStatusList",value = "单据状态(1-待收货 2-收货中 3-收货完成)")
    private List<String> orderStatusList;

    private Long asnOrderId;

    private Long orgId;

    private Long orderTypeId;

    @ApiModelProperty(name="orderStatus",value = "单据状态")
    private Byte orderStatus;

    @ApiModelProperty(name="orderTypeName",value = "单据类型")
    private String orderTypeName;

    @ApiModelProperty(name = "materialType",value = "物料类别")
    private Byte materialType;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    private String createTime;
}
