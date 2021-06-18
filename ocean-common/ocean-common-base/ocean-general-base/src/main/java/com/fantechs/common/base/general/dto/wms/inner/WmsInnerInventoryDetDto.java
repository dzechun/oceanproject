package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class WmsInnerInventoryDetDto extends WmsInnerInventoryDet implements Serializable {
    /**
     * 库位名称
     */
    @ApiModelProperty(name="storageName",value = "库位名称")
    @Excel(name = "库位名称", height = 20, width = 30,orderNum="5")
    @Transient
    private String storageName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="7")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="8")
    @Transient
    private String materialName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="27")
    @Transient
    private String supplierName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum = "28")
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum = "29")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum = "31")
    @Transient
    private String modifiedUserName;
}