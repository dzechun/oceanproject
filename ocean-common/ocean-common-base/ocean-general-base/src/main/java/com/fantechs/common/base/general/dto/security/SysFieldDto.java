package com.fantechs.common.base.general.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SysFieldDto implements Serializable {
    private static final long serialVersionUID = 3244097599032499131L;
    /**
     * 字段
     */
    @Transient
    @ApiModelProperty(name = "columnName",value = "字段")
    private String columnName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "columnComment",value = "字段说明")
    private String columnComment;

    /**
     * 字段类型
     */
    @Transient
    @ApiModelProperty(name = "columnComment",value = "字段类型")
    private String columnType;
}
