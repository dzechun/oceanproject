package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmTransferOrder;
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
public class OmTransferOrderDto extends OmTransferOrder implements Serializable {
    /**
     * 货主
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;

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
     * 总数量
     */
    @Transient
    @ApiModelProperty(name = "countOrderQty",value = "总数量")
    private BigDecimal countOrderQty;

    /**
     * 总体积
     */
    @Transient
    @ApiModelProperty(name = "countVolume",value = "总体积")
    private BigDecimal countVolume;

    /**
     * 总净重
     */
    @Transient
    @ApiModelProperty(name = "countNetWeight",value = "总净重")
    private BigDecimal countNetWeight;

    /**
     * 总毛重
     */
    @Transient
    @ApiModelProperty(name = "countGrossWeight",value = "总毛重")
    private BigDecimal countGrossWeight;

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
}
