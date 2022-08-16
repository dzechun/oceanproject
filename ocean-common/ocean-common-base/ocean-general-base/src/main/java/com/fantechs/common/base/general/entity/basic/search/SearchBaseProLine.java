package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchBaseProLine extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1363828868383986456L;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    private String proCode;

    /**
     * 线别名称
     */
    @ApiModelProperty(name="proName" ,value="线别名称")
    private String proName;

    /**
     * 线别描述
     */
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    private String proDesc;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId" ,value="车间ID")
    private String workShopId;

    /**
     * 工厂名称
     */
    @ApiModelProperty(name="factoryName" ,value="工厂名称")
    private String factoryName;

    /**
     * 车间名称
     */
    @ApiModelProperty(name="workShopName" ,value="车间名称")
    private String workShopName;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "queryMark",value = "查询方式标记")
    private Byte codeQueryMark;

    /**
     * 组织id
     *//*
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;*/
}
