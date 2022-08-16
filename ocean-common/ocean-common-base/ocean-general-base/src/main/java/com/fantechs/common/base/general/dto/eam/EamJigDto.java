package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigDto extends EamJig implements Serializable {

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
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="5")
    private String jigCategoryName;

    /**
     * 治具管理员
     */
    @Transient
    @ApiModelProperty(name = "userName",value = "治具管理员")
    @Excel(name = "治具管理员", height = 20, width = 30,orderNum="6")
    private String userName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 库区
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区")
    private String warehouseAreaName;

    /**
     * 工作区
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区")
    private String workingAreaCode;

    /**
     * 推荐库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "推荐库位")
    private String storageCode;

}
