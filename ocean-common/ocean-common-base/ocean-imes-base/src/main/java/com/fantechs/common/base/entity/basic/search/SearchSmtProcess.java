package com.fantechs.common.base.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtProcess extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -6658723130549341427L;

    /**
     * 工序代码
     */
    @ApiModelProperty(name="processCode" ,value="工序代码")
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 工序描述
     */
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    private String processDesc;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工段名称
     */
    @ApiModelProperty(name="sectionName" ,value="工段名称")
    private String sectionName;

    /**
     * 工序类别ID
     */
    @ApiModelProperty(name = "processCategoryId",value = "工序类别id")
    private Long processCategoryId;

    /**
     * 工序类别名称
     */
    @ApiModelProperty(name = "processCategoryName",value = "工序类别名称")
    private String processCategoryName;

    /**
     * 员工ID
     */
    @ApiModelProperty(name = "staffId",value = "员工ID")
    private Long staffId;

    /**
     * 查询类型
     */
    @ApiModelProperty(name = "searchType",value = "查询类型")
    private Integer searchType;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 是否报工扫描（0、否 1、是）
     */
    @ApiModelProperty(name="isJobScan" ,value="是否报工扫描")
    private Byte isJobScan;

    /**
     * 是否开工工扫描（0、否 1、是）
     */
    @ApiModelProperty(name= "isStartScan" ,value="是否开工工扫描")
    private Byte isStartScan;
}
