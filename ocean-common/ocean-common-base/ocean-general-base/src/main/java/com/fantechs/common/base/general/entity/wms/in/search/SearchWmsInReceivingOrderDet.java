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
public class SearchWmsInReceivingOrderDet extends BaseQuery implements Serializable {

    private Long receivingOrderId;

    private List<Byte> lineStatusList;

    @ApiModelProperty(name="ifFiltrate",value = "是否筛选（0，否  1，是）")
    private byte ifFiltrate;

    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialDesc",value = "物料描述")
    private String materialDesc;

    @ApiModelProperty(name="materialVersion",value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(name="receivingOrderCode",value = "收货作业单号")
    private String receivingOrderCode;
}
