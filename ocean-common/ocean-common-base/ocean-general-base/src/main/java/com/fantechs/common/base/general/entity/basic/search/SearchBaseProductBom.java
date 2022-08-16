package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by wcz on 2020/10/12.
 */
@Data
public class SearchBaseProductBom extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -6961850550811090030L;

    /**
     * 产品BOM编码
     */
    @ApiModelProperty(name="productBomCode" ,value="产品BOM编码")
    private String productBomCode;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;


    /**
     * 产品BOM ID
     */
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    private Long productBomId;

    /**
     * 产品BOMDET ID
     */
    @ApiModelProperty(name="productBomDetId" ,value="产品BOMDET ID")
    private Long productBomDetId;

    /**
     * 是否查询产品BOM
     */
    @ApiModelProperty(name="isBomDet" ,value="是否查询产品BOM")
    private Byte isBomDet;

    /**
     * BOM版本号
     */
    @ApiModelProperty(name="productBomVersion",value = "BOM版本号")
    private String productBomVersion;
}
