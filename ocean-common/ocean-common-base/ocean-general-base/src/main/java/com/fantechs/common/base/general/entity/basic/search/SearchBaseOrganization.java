package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchBaseOrganization extends BaseQuery implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId",value = "用户ID")
    private Long userId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="organizationCode",value = "组织编码")
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 组织描述
     */
    @ApiModelProperty(name="organizationDesc",value = "组织描述")
    private String organizationDesc;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    private Byte status;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
