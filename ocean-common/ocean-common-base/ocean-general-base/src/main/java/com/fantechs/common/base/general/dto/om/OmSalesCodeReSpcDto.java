package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OmSalesCodeReSpcDto extends OmSalesCodeReSpc implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode", value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName", value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 客户型号编码
     */
    @ApiModelProperty(name = "productModelCode", value = "客户型号编码")
    @Excel(name = "客户型号编码", height = 20, width = 30)
    private String productModelCode;

    /**
     * 客户型号名称
     */
    @ApiModelProperty(name = "productModelName", value = "客户型号名称")
    @Excel(name = "客户型号名称", height = 20, width = 30)
    private String productModelName;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;
}
