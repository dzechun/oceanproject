package com.fantechs.common.base.general.dto.mes.pm.history;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 销售订单与物料历史搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesHtOrderMaterialListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "订单ID",example = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单物料ID",example = "订单物料ID")
    private Long orderMaterialId;
}
