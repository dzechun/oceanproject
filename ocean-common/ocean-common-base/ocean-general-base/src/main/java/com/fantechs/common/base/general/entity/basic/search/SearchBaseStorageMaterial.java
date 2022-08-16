package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SearchBaseStorageMaterial extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 4672230387899893774L;
    /**
     * 仓库ID
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    private String warehouseAreaName;

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位编码")
    private String storageName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialDesc" ,value="物料编码")
    private String materialCode;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode" ,value="储位编码")
    private String storageCode;

    /**
     * 编码查询标记(1.等值查询 2.模糊查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 货主名称
     */
    @ApiModelProperty(name = "materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Byte status;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    private String materialVersion;

    /**
     * 上架策略
     */
    @ApiModelProperty(name = "putawayTactics",value = "上架策略")
    private Byte putawayTactics;

    /**
     * 补货策略
     */
    @ApiModelProperty(name = "replenishTactics",value = "补货策略")
    private Byte replenishTactics;
}
