package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerStocktakingDto extends WmsInnerStocktaking implements Serializable {

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseCode;

    /**
     * 仓库描述
     */
    @ApiModelProperty(name="warehouseDesc",value = "仓库描述")
    @Excel(name = "仓库描述", height = 20, width = 30,orderNum="")
    @Transient
    private String warehouseDesc;

    /**
     * 盘点员名称
     */
    @ApiModelProperty(name="stockistName",value = "盘点员名称")
    @Excel(name = "盘点员名称", height = 20, width = 30,orderNum="")
    @Transient
    private String stockistName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 组织代码
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织代码")
    private String organizationCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;
}
