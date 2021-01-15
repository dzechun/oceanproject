package com.fantechs.common.base.general.dto.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.in.WmsInOtherinDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInOtherinDetDto extends WmsInOtherinDet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="7")
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="8")
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    @Excel(name = "储位名称", height = 20, width = 30,orderNum="9")
    @ApiModelProperty(name="storageName" ,value="储位名称")
    private String storageName;

    @ApiModelProperty(name="organizationCode" ,value="组织编码")
    @Excel(name = "组织编码", height = 20, width = 30,orderNum="10")
    private String organizationCode;

    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Transient
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialName;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    @Transient
    private String materialDesc;
}
