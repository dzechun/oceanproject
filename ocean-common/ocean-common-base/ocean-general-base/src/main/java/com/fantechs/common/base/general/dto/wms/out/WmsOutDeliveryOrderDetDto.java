package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutDeliveryOrderDetDto extends WmsOutDeliveryOrderDet implements Serializable {

    @Excel(name = "成品编码", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="productMaterialCode" ,value="成品编码")
    private String productMaterialCode;

    @Excel(name = "成品描述", height = 20, width = 30,orderNum="")
    @ApiModelProperty(name="productMaterialName" ,value="成品描述（规格？）")
    private String productMaterialDesc;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    private String modifiedUserName;

}
