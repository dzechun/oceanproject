package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

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

    /**
     * 配置类别（0-系统配置，1-模块配置）默认0
     */
    @ApiModelProperty(name="category" ,value="配置类别")
    private Byte category;

    /**
     * 配置顺序（0-升序 ，1.降序）默认0
     */
    @ApiModelProperty(name="orderNum" ,value="配置顺序")
    private Byte orderNum;

    /**
     * 菜单ID
     */
    @ApiModelProperty(name="menuId" ,value="菜单ID")
    private Long menuId;

    /**
     * 菜单ID集合
     */
    @ApiModelProperty(name="menuIds" ,value="菜单ID集合")
    private List<Long> menuIds;

    /**
     * 是否使用缓存数据(0不使用、1使用)
     */
    @ApiModelProperty(name="ifHotData" ,value="是否使用缓存数据(0不使用、1使用)")
    private Byte ifHotData;

}
