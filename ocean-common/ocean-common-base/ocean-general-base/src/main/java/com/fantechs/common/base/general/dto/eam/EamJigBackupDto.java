package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamJigBackup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigBackupDto extends EamJigBackup implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="6")
    private String warehouseName;

    /**
     * 库区名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区名称")
    @Excel(name = "库区名称", height = 20, width = 30,orderNum="6")
    private String warehouseAreaName;

    /**
     * 工作区
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区")
    @Excel(name = "工作区", height = 20, width = 30,orderNum="6")
    private String workingAreaCode;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="6")
    private String storageCode;

}
