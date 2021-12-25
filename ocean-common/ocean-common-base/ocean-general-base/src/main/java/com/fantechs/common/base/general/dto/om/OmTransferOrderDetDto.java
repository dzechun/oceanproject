package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class OmTransferOrderDetDto extends OmTransferOrderDet implements Serializable {

    /**
     * 物料编码
     */
    @Transient
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;
    /**
     * 物料名称
     */
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 物料版本
     */
    @Transient
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="4")
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    private String materialVersion;

    /**
     * 包装单位
     */
    @Transient
    @ApiModelProperty(name = "mainUnit",value = "包装单位")
    private String mainUnit;

    /**
     * 体积
     */
    @Transient
    @ApiModelProperty(name = "volume",value = "体积")
    @Excel(name = "体积", height = 20, width = 30,orderNum="5")
    private BigDecimal volume;

    /**
     * 净重
     */
    @Transient
    @ApiModelProperty(name = "netWeight",value = "净重")
    @Excel(name = "净重", height = 20, width = 30,orderNum="6")
    private BigDecimal netWeight;

    /**
     * 毛重
     */
    @Transient
    @ApiModelProperty(name = "grossWeight",value = "毛重")
    @Excel(name = "毛重", height = 20, width = 30,orderNum="7")
    private BigDecimal grossWeight;
    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;

    /**
     *调出仓库
     */
    @Transient
    @ApiModelProperty(name = "outWarehouseName",value = "调出仓库")
    private String outWarehouseName;

    /**
     *调入仓库
     */
    @Transient
    @ApiModelProperty(name = "inWarehouseName",value = "调入仓库")
    private String inWarehouseName;

    /**
     *调拨订单号
     */
    @Transient
    @ApiModelProperty(name = "transferOrderCode",value = "调拨订单号")
    private String transferOrderCode;

    /**
     * 下发数量
     */
    @ApiModelProperty(name="issueQty",value = "下发数量")
    @Transient
    private BigDecimal issueQty;

    /**
     * 调出仓库ID
     */
    @ApiModelProperty(name="outWarehouseId",value = "调出仓库ID")
    @Transient
    private Long outWarehouseId;
}
