package com.fantechs.common.base.general.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

;
;

/**
 * Created by lfz on 2021/12/9.
 */
@Data
public class SearchSysConfiguration extends BaseQuery implements Serializable {
    /**
     * 配置Id
     */
    @ApiModelProperty(name="configurationId",value = "配置Id")
    private Long configurationId;

    /**
     * 配置名称
     */
    @ApiModelProperty(name="configurationName",value = "配置名称")
    private String configurationName;

    /**
     * 源对象
     */
    @ApiModelProperty(name="configurationSource",value = "源对象")
    private String configurationSource;

    /**
     * 目标对象
     */
    @ApiModelProperty(name="configurationTarget",value = "目标对象")
    private String configurationTarget;

    private static final long serialVersionUID = 1L;
}