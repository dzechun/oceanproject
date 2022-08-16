package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/23.
 */
@Data
public class SearchBaseWarehouseArea extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -750262365792251512L;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name="warehouseAreaId" ,value="仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 仓库区域编码
     */
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    private String warehouseAreaCode;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    private String warehouseAreaName;

    /**
     * 仓库区域描述
     */
    @ApiModelProperty(name="warehouseAreaDesc" ,value="仓库区域描述")
    private String warehouseAreaDesc;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;
}
