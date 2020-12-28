package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchWmsOutPurchaseReturn extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购退货单号
     */
    @ApiModelProperty(name="purchaseReturnCode",value = "采购退货单号")
    private String purchaseReturnCode;


}
