package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsOutOtherout extends BaseQuery implements Serializable {


    /**
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    private String otheroutCode;

    /**
     * 单据状态（0-待出库 1-出货中 2-出货完成）
     */
    @ApiModelProperty(name="otheroutStatus",value = "单据状态（0-待出库 1-出货中 2-出货完成）")
    private Byte otheroutStatus;

    /**
     * 操作人名称
     */
    @ApiModelProperty(name="operatorName",value = "操作人名称")
    private String operatorName;

}
