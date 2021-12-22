package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsOutDeliveryReqOrderDetDto extends WmsOutDeliveryReqOrderDet implements Serializable {

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 出库人
     */
    @Transient
    @ApiModelProperty(name = "deliveryUserName",value = "出库人")
    @Excel(name = "出库人", height = 20, width = 30,orderNum="8")
    private String deliveryUserName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="5")
    private String materialVersion;

    /**
     * 出货通知单号
     */
    @Transient
    @ApiModelProperty(name = "deliveryReqOrderCode",value = "出货通知单号")
    //@Excel(name = "出货通知单号", height = 20, width = 30,orderNum="23")
    private String deliveryReqOrderCode;

    /**
     * 下发数量
     */
    @ApiModelProperty(name="issueQty",value = "下发数量")
    @Transient
    private BigDecimal issueQty;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Transient
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Transient
    private String warehouseName;

}
