package com.fantechs.common.base.general.entity.basic.search;

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
public class SearchBaseProductProcessRoute extends BaseQuery implements Serializable {


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
     *  物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     *  产线名称
     */
    @ApiModelProperty(name="proName" ,value="产线名称")
    private String proName;

    /**
     *  产品型号名称
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    private String productModelName;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(name="routeCode" ,value="工艺路线编码")
    private String routeCode;

}
