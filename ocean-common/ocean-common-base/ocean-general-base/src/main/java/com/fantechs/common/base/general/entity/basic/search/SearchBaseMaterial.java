package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/15 11:20
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseMaterial extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -4397503096388466915L;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

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
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 物料属性查询标记（传1表示查询成品和半成品）
     */
    @ApiModelProperty(name = "propertyQueryMark",value = "物料属性查询标记（传1表示查询成品和半成品）")
    private Integer propertyQueryMark;
}