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

    @ApiModelProperty(name="planName" ,value="PlanName")
    @Column(name = "plan_name")
    private String planName;

    @ApiModelProperty(name="barcode" ,value="产品条码(万宝场内码)")
    @Column(name = "barcode")
    private String barcode;

    @ApiModelProperty(name="customerBarcode" ,value="客户条码（三星产品唯一码）")
    @Column(name = "customer_barcode")
    private String customerBarcode;

    @ApiModelProperty(name="stationId" ,value="工站")
    @Column(name = "station_id")
    private String stationId;

    @ApiModelProperty(name="status",value = "过站状态")
    @Column(name = "status")
    private Byte status;

    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Column(name = "create_time")
    private String createTime;

}
