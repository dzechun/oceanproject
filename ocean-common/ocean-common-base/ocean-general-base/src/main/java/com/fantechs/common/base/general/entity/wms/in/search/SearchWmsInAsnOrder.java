package com.fantechs.common.base.general.entity.wms.in.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
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
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;
}
