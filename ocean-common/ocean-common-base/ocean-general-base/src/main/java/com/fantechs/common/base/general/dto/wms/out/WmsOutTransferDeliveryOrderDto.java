package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsOutTransferDeliveryOrderDto extends WmsOutDeliveryOrder implements Serializable {

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="5")
    private String materialOwnerName;

    /**
     * 总数量
     */
    @ApiModelProperty(name="totalPackingQty",value = "总数量")
    @Excel(name = "总数量", height = 20, width = 30,orderNum="6")
    private BigDecimal totalPackingQty;

    /**
     * 拣货数量
     */
    @ApiModelProperty(name="totalPickingQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="7")
    private BigDecimal totalPickingQty;

    /**
     * 发货数量
     */
    @ApiModelProperty(name="totalDispatchQty",value = "发货数量")
    @Excel(name = "发货数量", height = 20, width = 30,orderNum="8")
    private BigDecimal totalDispatchQty;

    @Transient
    @ApiModelProperty(name="storageCode" ,value="库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="4")
    private String storageCode;

    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="3")
    private String warehouseName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationCode",value = "组织代码")
    private String organizationCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="21")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="23")
    private String modifiedUserName;


}
