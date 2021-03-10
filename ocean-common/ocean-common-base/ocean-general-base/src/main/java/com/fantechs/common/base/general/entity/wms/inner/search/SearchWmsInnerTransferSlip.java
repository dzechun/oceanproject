package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;


@Data
public class SearchWmsInnerTransferSlip extends BaseQuery {

    /**
     * 调拨单号
     */
    @ApiModelProperty(name="transferSlipCode",value = "调拨单号")
    private String transferSlipCode;

    /**
     * 操作人
     */
    @ApiModelProperty(name="modifiedUserName",value = "操作人")
    private String modifiedUserName;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    private String processorUserName;

    /**
     * 单据状态（0-待调拨 1-调拨中 2-调拨完成）
     */
    @ApiModelProperty(name="transferSlipStatus",value = "单据状态（0-待调拨 1-调拨中 2-调拨完成）")
    private Byte transferSlipStatus;
}
