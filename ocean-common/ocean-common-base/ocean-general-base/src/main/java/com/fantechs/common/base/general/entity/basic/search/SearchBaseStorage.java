package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBaseStorage extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -6508024716295865028L;

    /**
     * 储位类型
     */
    @ApiModelProperty(name = "storageType",value = "储位类型")
    private Byte storageType;

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
     * 储位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    private String storageDesc;

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
     * 工作区编码
     */
    @ApiModelProperty(name = "workingAreaCode",value = "工作区编码")
    private String workingAreaCode;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "queryMark",value = "查询方式标记")
    private Byte codeQueryMark;

    /**
     * 查询可放托盘数XX以上的库位
     */
    @ApiModelProperty(name = "minSurplusCanPutSalver",value = "最小剩余可放托盘数")
    private int minSurplusCanPutSalver;
}
