package com.fantechs.common.base.general.dto.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EngPackingOrderSummaryDetDto extends EngPackingOrderSummaryDet implements Serializable {


    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    @Column(name = "material_name")
    private String materialName;


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

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 收货库位
     */
    @Transient
    @ApiModelProperty(name = "receivingStorageName",value = "收货库位")
    private String receivingStorageName;

    /**
     * 上架库位
     */
    @Transient
    @ApiModelProperty(name = "putawayStorageName",value = "上架库位")
    private String putawayStorageName;

    /**
     * 取消数量
     */
    @Transient
    @ApiModelProperty(name = "cancelQty",value = "取消数量")
    private BigDecimal cancelQty;

    /**
     * 按钮类型（1-确认 2-收货确认）
     */
    @Transient
    @ApiModelProperty(name = "buttonType",value = "按钮类型（1-确认 2-收货确认）")
    private Byte buttonType;

    /**
     * 装箱清单id
     */
    @ApiModelProperty(name = "packingOrderId",value = "装箱清单id")
    private Long packingOrderId;
}
