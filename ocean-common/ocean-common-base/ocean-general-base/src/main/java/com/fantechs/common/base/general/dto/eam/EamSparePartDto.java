package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamSparePartDto extends EamSparePart implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 备用件类别名称
     */
    @Transient
    @ApiModelProperty(name = "sparePartCategoryName",value = "备用件类别名称")
    @Excel(name = "备用件类别名称", height = 20, width = 30,orderNum="5")
    private String sparePartCategoryName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 库位编码
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位名称")
    private String storageCode;

    /**
     * 工作区编码
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区编码")
    private String workingAreaCode;
}
