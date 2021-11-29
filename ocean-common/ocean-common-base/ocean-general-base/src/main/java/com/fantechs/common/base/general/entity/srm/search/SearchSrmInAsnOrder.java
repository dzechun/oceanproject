package com.fantechs.common.base.general.entity.srm.search;

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
public class SearchSrmInAsnOrder extends BaseQuery implements Serializable {
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
     * 单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)
     */
    @ApiModelProperty(name="orderStatusList",value = "单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)")
    private List<String> orderStatusList;

    /**
     * id
     */
    private Long asnOrderId;

    private Long orgId;

    /**
     * 单据类型id
     */
    private Long orderTypeId;

    /**
     * 单据状态
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-保存 2-提交 3-审核通过 4-审核未通过 5-已预约 6-发货)")
    private Byte orderStatus;

    /**
     * 单据类型名称
     */
    @ApiModelProperty(name="orderTypeName",value = "单据类型名称")
    private String orderTypeName;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;
}
