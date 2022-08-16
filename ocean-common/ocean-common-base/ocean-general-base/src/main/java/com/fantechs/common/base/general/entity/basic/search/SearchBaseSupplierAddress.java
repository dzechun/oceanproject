package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


@Data
public class SearchBaseSupplierAddress extends BaseQuery implements Serializable {
    /**
     * 供应商关联地址ID
     */
    @ApiModelProperty(name="supplierAddressId",value = "供应商关联地址ID")
    @Id
    @Column(name = "supplier_address_id")
    private Long supplierAddressId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    private static final long serialVersionUID = 1L;
}
