package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SrmDeliveryAppointDto extends SrmDeliveryAppoint implements Serializable {

    /**
     * 供应商名称
     */
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="1")
    private String supplierName;

    /**
     * 送货仓库名称
     */
    @ApiModelProperty(name = "organizationName",value = "送货仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="5")
    private String warehouseName;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


    /**
     * 车位信息详情
     */
    private List<SrmAppointDeliveryReAsnDto> list;


    private static final long serialVersionUID = 1L;
}
