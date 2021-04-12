package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @date 2020-12-04 14:39:37
 */
@Data
public class SearchWmsInnerStorageInventoryDet extends BaseQuery implements Serializable {

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    private String storageName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 储位库存ID
     */
    @ApiModelProperty(name="storageInventoryId",value = "储位库存ID")
    private Long storageInventoryId;

    /**
     * 入库单号
     */
    @ApiModelProperty(name="godownEntry",value = "入库单号")
    private String godownEntry;

    /**
     * 物料条码编码
     */
    @ApiModelProperty(name="materialBarcodeCode",value = "物料条码编码")
    private String materialBarcodeCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name = "contractNo", value = "合同号")
    private String contractCode;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    private Long storageId;

    /**
     * 是否绑定
     */
    @ApiModelProperty(name="isBinding",value = "是否绑定")
    private Byte isBinding;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId", value = "物料ID")
    private Long materialId;

}
