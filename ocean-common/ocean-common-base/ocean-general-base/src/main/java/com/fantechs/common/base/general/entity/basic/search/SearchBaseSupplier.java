package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/9/27
 */
@ApiModel
@Data
public class SearchBaseSupplier extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -2927770419793897574L;
    @ApiModelProperty("供应商id")
    private Long supplierId;
    @ApiModelProperty("供应商代码")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("供应商描述")
    private String supplierDesc;
    @ApiModelProperty("国家名称")
    private String countryName;
    @ApiModelProperty("大区名称")
    private String regionName;
    @ApiModelProperty("身份标识（1、供应商 2、客户）")
    private Byte supplierType;
    @ApiModelProperty("供应商地址")
    private String addressDetail;
    @ApiModelProperty(name = "queryMark",value = "查询方式标记")
    private Byte codeQueryMark;
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;
}
