package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseShipmentEnterpriseImport implements Serializable {

    /**
     * 物流商编码(必填)
     */
    @Excel(name = "物流商编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="shipmentEnterpriseCode" ,value="物流商编码(必填)")
    private String shipmentEnterpriseCode;

    /**
     * 物流商名称
     */
    @Excel(name = "物流商名称", height = 20, width = 30)
    @ApiModelProperty(name="shipmentEnterpriseName" ,value="物流商名称")
    private String shipmentEnterpriseName;

    /**
     * 运输类型(1-快递 2-海运 3-空运)
     */
    @Excel(name = "运输类型(1-快递 2-海运 3-空运)", height = 20, width = 30)
    @ApiModelProperty(name="transportCategoryId" ,value="运输类型(1-快递 2-海运 3-空运)")
    private Integer transportCategoryId;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkman",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    private String linkman;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="phone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    private String phone;
}
