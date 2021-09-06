package com.fantechs.provider.wanbao.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "middle_material")
public class MiddleSaleOrder implements Serializable {

    @Id
    @Column(name = "middle_sale_order_id")
    private Long MiddleSaleOrderId;

    @ApiModelProperty(name="materialId" ,value="物料id")
    @Column(name = "material_id")
    private Long materialId;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Column(name = "material_desc")
    private String materialDesc;

    @ApiModelProperty(name="voltage",value = "电压")
    @Column(name = "voltage")
    private String voltage;

    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    @Column(name = "product_model_id")
    private Long productModelId;

    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Column(name = "product_model_code")
    private String productModelCode;

    @ApiModelProperty(name="materialProperty",value = "物料属性(0.半成品，1.成品)")
    @Column(name = "material_property")
    private Byte materialProperty;

    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Column(name = "modified_time")
    private Date modifiedTime;
}
