package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSrmDeliveryAppoint extends BaseQuery implements Serializable {

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 供应商
     */
    @ApiModelProperty(name = "supplierName",value = "供应商")
    private String supplierName;

    /**
     * 车牌号
     */
    @ApiModelProperty(name = "carLicenseCode",value = "车牌号")
    private String carLicenseCode;

    /**
     * 联系人
     */
    @ApiModelProperty(name = "linkManName",value = "联系人")
    private String linkManName;

    /**
     * 预约状态(1-预约待审核 2-预约失败 3-已预约 4-取消待审核5-已取消 6-发货完成)
     */
    @ApiModelProperty(name="appointStatus",value = "预约状态(1-预约待审核 2-预约失败 3-已预约 4-取消待审核5-已取消 6-发货完成)")
    private Byte appointStatus;

    private static final long serialVersionUID = 1L;
}
