package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchBaseOrderType extends BaseQuery implements Serializable {


    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;

    /**
     * 单据类型名称
     */
    @ApiModelProperty(name="orderTypeName",value = "单据类型名称")
    private String orderTypeName;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 业务类型
     */
    @ApiModelProperty(name="businessType",value = "业务类型")
    private Integer businessType;
}
