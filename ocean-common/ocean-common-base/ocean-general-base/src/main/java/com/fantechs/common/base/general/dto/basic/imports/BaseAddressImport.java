package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseAddressImport implements Serializable {

    /**
     * 省
     */
    @ApiModelProperty(name="province",value = "省")
    @Excel(name = "省", height = 20, width = 30)
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(name="city",value = "市")
    @Excel(name = "市", height = 20, width = 30)
    private String city;

    /**
     * 区/县
     */
    @ApiModelProperty(name="classify",value = "区/县")
    @Excel(name = "区/县", height = 20, width = 30)
    private String classify;

    /**
     * 详细地址（必填）
     */
    @ApiModelProperty(name="addressDetail",value = "详细地址（必填）")
    @Excel(name = "详细地址（必填）", height = 20, width = 30)
    private String addressDetail;

    /**
     * 邮政编码
     */
    @ApiModelProperty(name="postCode",value = "邮政编码")
    @Excel(name = "邮政编码", height = 20, width = 30)
    private String postCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

}
