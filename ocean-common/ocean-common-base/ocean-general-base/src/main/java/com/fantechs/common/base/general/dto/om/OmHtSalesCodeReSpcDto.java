package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmHtSalesCodeReSpc;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmHtSalesCodeReSpcDto extends OmHtSalesCodeReSpc implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode", value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName", value = "物料名称")
    private String materialName;

    /**
     * 客户型号编码
     */
    @ApiModelProperty(name = "productModelCode", value = "客户型号编码")
    private String productModelCode;

    /**
     * 客户型号名称
     */
    @ApiModelProperty(name = "productModelName", value = "客户型号名称")
    private String productModelName;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
