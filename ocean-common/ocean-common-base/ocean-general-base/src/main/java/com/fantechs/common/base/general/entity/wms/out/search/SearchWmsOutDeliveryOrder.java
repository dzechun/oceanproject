package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsOutDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 销售出库单号
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "销售出库单号")
    private String deliveryOrderCode;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 相关单号1
     */
    @ApiModelProperty(name="relatedOrderCode1",value = "相关单号1")
    private String relatedOrderCode1;

    /**
     * 收货人
     */
    @ApiModelProperty(name="consignee" ,value="收货人")
    private String consignee;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName" ,value="联系人")
    private String linkManName;

    /**
     * 联系方式
     */
    @ApiModelProperty(name="linkManPhone" ,value="联系方式")
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber" ,value="传真")
    private String faxNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(name="emailAddress" ,value="邮箱")
    private String emailAddress;

}
