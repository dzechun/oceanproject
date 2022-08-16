package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/17
 */
@Data
public class SearchBaseLabel extends BaseQuery implements Serializable {
    /**
     * 标签代码
     */
    @ApiModelProperty(name="labelCode",value = "标签代码")
    @Column(name = "label_code")
    private String labelCode;

    /**
     * 标签名称
     */
    @ApiModelProperty(name="labelName",value = "标签名称")
    @Column(name = "label_name")
    private String labelName;

    /**
     * 描述
     */
    @ApiModelProperty(name="labelDesc",value = "描述")
    @Column(name = "label_desc")
    private String labelDesc;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    @Column(name = "label_category_name")
    private String labelCategoryName;

    /**
     * 编码查询标记(1.等值查询 2.模糊查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
