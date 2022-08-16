package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamRequisitionOrderDto extends EamRequisitionOrder implements Serializable {

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
     * 部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门")
    private String deptName;

    /**
     * 领用人名称
     */
    @Transient
    @ApiModelProperty(name = "requisitionUserName",value = "领用人名称")
    private String requisitionUserName;

}
