package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 包装管理搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesPackageManagerListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "条码",example = "条码")
    private String barcode;
    @ApiModelProperty(value = "工单编码",example = "工单编码")
    private String workOrderCode;
    @ApiModelProperty(value = "包装管理号",example = "包装管理号")
    private String packageManagerCode;
    @ApiModelProperty(value = "父级ID",example = "父级ID")
    private Long parentId;
}
