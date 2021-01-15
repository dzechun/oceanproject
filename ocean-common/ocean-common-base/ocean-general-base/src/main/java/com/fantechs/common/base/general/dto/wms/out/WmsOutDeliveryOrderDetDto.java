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

    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    private String modifiedUserName;

    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

}
