package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/3.
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchSmtProductProcessRoute extends BaseQuery implements Serializable {


    /**
     * 产品类别(0.All(*) 1.线别名称 2.产品型号 3.产品料号)
     */
    @ApiModelProperty(name="productType" ,value="产品类别")
    private Integer productType;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="proLineId" ,value="线别ID")
    private Long proLineId;

    /**
     *  产品型号ID
     */
    @ApiModelProperty(name="productModelId" ,value="产品型号ID")
    private Long productModelId;


    /**
     *  物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;


    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线描述
     */
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
    private String routeDesc;
}
