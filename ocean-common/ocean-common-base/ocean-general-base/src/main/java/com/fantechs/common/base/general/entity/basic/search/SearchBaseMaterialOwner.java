package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangzhongwu
 * @create 2021-04-22 18:25
 */
@Data
public class SearchBaseMaterialOwner extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 397172384353445224L;

    /**
     * ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息ID")
    private Long materialOwnerId;

    /**
     * 货主编码
     */
    @ApiModelProperty(name="materialOwnerCode",value = "货主编码")
    private String materialOwnerCode;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

}
