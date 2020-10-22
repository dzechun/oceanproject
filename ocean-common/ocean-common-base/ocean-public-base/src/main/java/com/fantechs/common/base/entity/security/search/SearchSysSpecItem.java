package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchSysSpecItem  extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -4072661553411752786L;
    /**
     * 配置项ID
     */
    @Id
    @Column(name = "spec_id")
    @ApiModelProperty(name="specId" ,value="配置项ID")
    private Long specId;

    /**
     * 配置项代码
     */
    @Column(name = "spec_code")
    @ApiModelProperty(name="specCode" ,value="配置项代码")
    private String specCode;

    /**
     * 配置项名称
     */
    @Column(name = "spec_name")
    @ApiModelProperty(name="specName" ,value="配置项名称")
    private String specName;

    /**
     * 参数
     */
    @Column(name = "para")
    @ApiModelProperty(name="para" ,value="参数")
    private String para;

    /**
     * 参数值
     */
    @Column(name = "para_value")
    @ApiModelProperty(name="paraValue" ,value="参数值")
    private String paraValue;


}