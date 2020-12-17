package com.fantechs.common.base.general.entity.bcm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class SearchBcmLabelCategory extends BaseQuery implements Serializable {
    /**
     * 标签类别代码
     */
    @ApiModelProperty(name="labelCategoryCode",value = "标签类别代码")
    @Column(name = "label_category_code")
    private String labelCategoryCode;

    /**
     * 标签类别名称
     */
    @ApiModelProperty(name="labelCategoryName",value = "标签类别名称")
    @Column(name = "label_category_name")
    private String labelCategoryName;

    /**
     * 描述
     */
    @ApiModelProperty(name="labelCategoryDesc",value = "描述")
    @Column(name = "label_category_desc")
    private String labelCategoryDesc;
}
