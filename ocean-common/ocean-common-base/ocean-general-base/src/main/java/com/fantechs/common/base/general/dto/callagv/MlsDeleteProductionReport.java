package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MlsDeleteProductionReport {

    @ApiModelProperty(name = "isUnbound", value = "是否全部解绑 0-否 1-是")
    private Integer isUnbound;

    @ApiModelProperty(name = "containerCode", value = "容器编码")
    private String containerCode;

    @ApiModelProperty(name = "userName", value = "工号")
    private String userName;

    @ApiModelProperty(name = "materialDetails", value = "出入库商品明细")
    private List<GoodsDetail> materialDetails;
}
