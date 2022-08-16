package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SearchBaseTeam extends BaseQuery implements Serializable {

    /**
     * 班组代码
     */
    @ApiModelProperty(name="teamCode",value = "班组代码")
    private String teamCode;

    /**
     * 班组名称
     */
    @ApiModelProperty(name="teamName",value = "班组名称")
    private String teamName;

    /**
     * 班组描述
     */
    @ApiModelProperty(name="teamDesc",value = "班组描述")
    private String teamDesc;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    private Long workShopId;

    /**
     * 厂别名称
     */
    @ApiModelProperty(name="factoryName",value = "厂别名称")
    private String factoryName;

    /**
     * 车间名称
     */
    @ApiModelProperty(name="workShopName",value = "车间名称")
    private String workShopName;
}
