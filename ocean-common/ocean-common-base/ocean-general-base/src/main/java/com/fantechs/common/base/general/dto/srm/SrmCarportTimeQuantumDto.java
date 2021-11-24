package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SrmCarportTimeQuantumDto extends SrmCarportTimeQuantum implements Serializable {

    /**
     * 车位数量
     */
    @ApiModelProperty(name="carportCount",value = "车位数量")
    private Integer carportCount;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name = "consigneeName",value = "收货人名称")
    private String consigneeName;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
