package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseStaff extends BaseQuery implements Serializable {

    /**
     * 员工编码
     */
    @ApiModelProperty(name="staffCode",value = "员工编码")
    private String staffCode;

    /**
     * 员工名称
     */
    @ApiModelProperty(name="staffName",value = "员工名称")
    private String staffName;

    /**
     * 员工描述
     */
    @ApiModelProperty(name="staffDesc",value = "员工描述")
    private String staffDesc;

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
     * 车间编码
     */
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 厂别编码
     */
    @ApiModelProperty(name = "factoryCode",value = "厂别编码")
    private String factoryCode;

    /**
     * 厂别名称
     */
    @ApiModelProperty(name = "factoryName",value = "厂别名称")
    private String factoryName;

    /**
     * 查询类型
     */
    @ApiModelProperty(name = "searchType",value = "查询类型")
    private Integer searchType;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

}
