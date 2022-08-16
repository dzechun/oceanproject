package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigMaintainOrderDto extends EamJigMaintainOrder implements Serializable {

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="2")
    private String jigBarcode;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="3")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="4")
    private String jigName;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="5")
    private String jigCategoryName;

    /**
     * 治具保养项目编码
     */
    @Transient
    @ApiModelProperty(name = "jigMaintainProjectCode",value = "治具保养项目编码")
    @Excel(name = "治具保养项目编码", height = 20, width = 30,orderNum="6")
    private String jigMaintainProjectCode;

    /**
     * 治具保养项目名称
     */
    @Transient
    @ApiModelProperty(name = "jigMaintainProjectName",value = "治具保养项目名称")
    @Excel(name = "治具保养项目名称", height = 20, width = 30,orderNum="7")
    private String jigMaintainProjectName;

}
