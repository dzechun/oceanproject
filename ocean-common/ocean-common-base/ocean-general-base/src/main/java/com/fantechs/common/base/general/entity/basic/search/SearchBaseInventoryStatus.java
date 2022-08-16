package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInventoryStatus extends BaseQuery implements Serializable {

    /**
     * 库存状态id
     */
    @ApiModelProperty(name="inventoryStatusId" ,value="库存状态id")
    private Long inventoryStatusId;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    /**
     * 货主id
     */
    @ApiModelProperty(name="materialOwnerId" ,value="货主id")
    private Long materialOwnerId;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName" ,value="货主名称")
    private String materialOwnerName;

    /**
     * 状态名称
     */
    @ApiModelProperty(name="inventoryStatusName" ,value="状态名称")
    private String inventoryStatusName;

    /**
     * 名称查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "nameQueryMark",value = "名称查询标记(设为1做等值查询)")
    private Integer nameQueryMark;

    /**
     * 是否默认状态(0-否 1-是)
     */
    @ApiModelProperty(name="ifDefaultStatus",value = "是否默认状态(0-否 1-是)")
    private Byte ifDefaultStatus;

    /**
     * 是否可发(0-否 1-是)
     */
    @ApiModelProperty(name="ifCanStoreIssue",value = "是否可发(0-否 1-是)")
    private Byte ifCanStoreIssue;

}
