package com.fantechs.common.base.general.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SysTableDto implements Serializable {
    private static final long serialVersionUID = 3244097599032499131L;
    /**
     * 数据表
     */
    @Transient
    @ApiModelProperty(name = "tableName",value = "数据表")
    private String tableName;

    /**
     * 表说明
     */
    @Transient
    @ApiModelProperty(name = "tableComment",value = "表说明")
    private String tableComment;
}
