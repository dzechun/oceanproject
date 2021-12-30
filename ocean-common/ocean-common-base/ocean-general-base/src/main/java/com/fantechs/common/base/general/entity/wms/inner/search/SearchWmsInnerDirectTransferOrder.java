package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsInnerDirectTransferOrder extends BaseQuery implements Serializable {

    /**
     * 直接调拨单号
     */
    @ApiModelProperty(name="directTransferOrderCode",value = "直接调拨单号")
    private String directTransferOrderCode;

    /**
     * 单据状态(1-待作业、2-作业中、3-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待作业、2-作业中、3-完成)")
    private Byte orderStatus;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}
