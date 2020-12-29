package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SearchSrmDeliveryNoteDet extends BaseQuery implements Serializable {

    /**
     * 送货通知ID
     */
    @ApiModelProperty(name="deliveryNoteId",value = "送货通知ID")
    private Long deliveryNoteId;

    /**
     * 订单类别
     */
    @ApiModelProperty(name="orderType",value = "订单类别")
    private Byte orderType;


    private static final long serialVersionUID = 1L;
}
