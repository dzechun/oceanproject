package com.fantechs.provider.wanbao.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "middle_product")
public class MiddleProduct implements Serializable {

    @Id
    @ApiModelProperty(name="productId" ,value="id")
    @Column(name = "product_id")
    private String productId;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="workOrderCode" ,value="工单编码")
    @Column(name = "work_order_code")
    private String workOrderCode;

    @ApiModelProperty(name="barcode" ,value="产品条码(万宝场内码)")
    @Column(name = "barcode")
    private String barcode;

    @ApiModelProperty(name="customerBarcode" ,value="客户条码（三星产品唯一码）")
    @Column(name = "customer_barcode")
    private String customerBarcode;

    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Column(name = "create_time")
    private String createTime;

}
