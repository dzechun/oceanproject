package com.fantechs.common.base.general.entity.log.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchSmtEmpOperationLog extends BaseQuery implements Serializable {

    /**
     * 单据号
     */
    @ApiModelProperty(name = "documentCode", value = "单据号")
    @Transient
    private String documentCode;

    /**
     * 功能菜单
     */
    @ApiModelProperty(name = "functionMenu", value = "功能菜单")
    @Transient
    private String functionMenu;

    /**
     * 操作类型
     */
    @ApiModelProperty(name = "operationType", value = "操作类型")
    @Transient
    private String operationType;

    /**
     * 操作人
     */
    @ApiModelProperty(name = "operationPerson", value = "操作人")
    @Transient
    private String operationPerson;

}
