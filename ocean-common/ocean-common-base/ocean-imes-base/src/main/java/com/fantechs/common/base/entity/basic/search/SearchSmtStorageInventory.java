package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/12/2 17:33
 */
@Data
public class SearchSmtStorageInventory extends BaseQuery implements Serializable {

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
     * 物料ID
     */
    @ApiModelProperty(name = "storageCode",value = "物料ID")
    private String materialId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageName",value = "储位ID")
    private String storageId;

    /**
     * 等级
     */
    @ApiModelProperty(name = "level",value = "等级")
    private String level;

}
