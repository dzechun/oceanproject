package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerDirectTransferOrderDetDto extends WmsInnerDirectTransferOrderDet implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name = "materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    private String materialVersion;


    /**
     * 调出仓库
     */
    @ApiModelProperty(name = "outWarehouseName",value = "调出仓库")
    private String outWarehouseName;

    /**
     * 调出库位名称
     */
    @Transient
    @ApiModelProperty(name = "outStorageName",value = "调出库位名称")
    private String outStorageName;

    /**
     * 调入仓库
     */
    @ApiModelProperty(name = "inWarehouseName",value = "调出仓库")
    private String inWarehouseName;

    /**
     * 调入库位名称
     */
    @Transient
    @ApiModelProperty(name = "inStorageName",value = "调出库位名称")
    private String inStorageName;



    /**
     * 调出库位编码
     */
    @Transient
    @ApiModelProperty(name = "outStorageCode",value = "调出库位编码")
    private String outStorageCode;

    /**
     * 调入库位编码
     */
    @Transient
    @ApiModelProperty(name = "inStorageCode",value = "调入库位编码")
    private String inStorageCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30,orderNum = "36")
    @Transient
    private String organizationName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum = "37")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum = "35")
    @Transient
    private String modifiedUserName;


    private static final long serialVersionUID = 1L;
}
