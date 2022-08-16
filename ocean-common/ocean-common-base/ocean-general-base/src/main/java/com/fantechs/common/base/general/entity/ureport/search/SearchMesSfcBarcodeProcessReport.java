package com.fantechs.common.base.general.entity.ureport.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchMesSfcBarcodeProcessReport extends BaseQuery implements Serializable {

    /**
     * 成品条码
     */
    @ApiModelProperty(name="barcode",value = "成品条码")
    private String barcode;

    /**
     * 部件条码
     */
    @ApiModelProperty(name="partBarcode",value = "部件条码")
    private String partBarcode;

    /**
     * 客户条码
     */
    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    private String customerBarcode;

    /**
     * tab标签ID（1、条码过站记录 2、检验记录 3、包箱记录 4、栈板记录 5、返工记录 6、设备参数记录 7、装配记录）
     */
    @ApiModelProperty(name="tabId",value = "tab标签ID（1、条码过站记录 2、检验记录 3、包箱记录 4、栈板记录 5、返工记录 6、设备参数记录 7、装配记录）")
    private Byte tabId;


}
