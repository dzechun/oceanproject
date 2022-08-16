package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInAndOutRule extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -3896088051789821762L;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    /**
     * 出入库规则名称
     */
    @ApiModelProperty(name="inAndOutRuleName" ,value="出入库规则名称")
    private String inAndOutRuleName;

    /**
     * 创建账号
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号")
    private String createUserName;

    /**
     * 类型(1-入库 2-出库)
     */
    @ApiModelProperty(name="category" ,value="类型(1-入库 2-出库)")
    private Byte category;

}
