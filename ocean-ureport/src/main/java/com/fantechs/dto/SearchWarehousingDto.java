package com.fantechs.dto;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWarehousingDto extends BaseQuery implements Serializable {

    @ApiModelProperty(name="workShopName" ,value="车间")
    private String workShopName;

    @ApiModelProperty(name="proName",value = "产线")
    private String proName;

    @ApiModelProperty(name="site",value = "站点(1包装、2入库、3上架、4未投产)")
    private Byte site;

    @ApiModelProperty(name="passStatus",value = "状态（0OK,1NG）")
    private String passStatus;

    @ApiModelProperty(name="siteStatus",value = "站点状态(1未过站，2已打包未入库，3已入库未上架，4正常)")
    private Byte siteStatus;

}
