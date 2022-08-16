package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseShipmentEnterprise extends BaseQuery implements Serializable {

    /**
     * 物流商编码
     */
    @ApiModelProperty(name="shipmentEnterpriseCode",value = "物流商编码")
    private String shipmentEnterpriseCode;

    /**
     * 物流商名称
     */
    @ApiModelProperty(name="shipmentEnterpriseName",value = "物流商名称")
    private String shipmentEnterpriseName;

    /**
     * 物流商描述
     */
    @ApiModelProperty(name="shipmentEnterpriseDesc",value = "物流商描述")
    private String shipmentEnterpriseDesc;

    /**
     * 运输类型ID
     */
    @ApiModelProperty(name="transportCategoryId",value = "运输类型ID")
    private Byte transportCategoryId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;
}
