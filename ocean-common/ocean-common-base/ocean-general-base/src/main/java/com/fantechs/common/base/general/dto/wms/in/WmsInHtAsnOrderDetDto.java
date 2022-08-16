package com.fantechs.common.base.general.dto.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInHtAsnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Data
public class WmsInHtAsnOrderDetDto extends WmsInHtAsnOrderDet implements Serializable {
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name="storageCode",value = "库位")
    private String storageCode;

    /**
     * 库存状态
     */
    @Transient
    @ApiModelProperty(name = "inventoryStatusName",value = "库存状态")
    private String inventoryStatusName;
    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;
}
