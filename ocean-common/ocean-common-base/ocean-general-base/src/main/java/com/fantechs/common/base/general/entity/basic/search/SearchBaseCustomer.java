package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/27
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseCustomer extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -7401165346625030964L;
    /**
     * 客户代码
     */
    @ApiModelProperty(name="customerCode" ,value="客户代码")
    private String customerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;

    /**
     * 客户描述
     */
    @ApiModelProperty(name="customerDesc" ,value="客户描述")
    private String customerDesc;

}
