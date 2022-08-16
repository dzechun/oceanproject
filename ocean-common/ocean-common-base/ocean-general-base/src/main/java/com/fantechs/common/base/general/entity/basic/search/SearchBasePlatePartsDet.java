package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchBasePlatePartsDet extends BaseQuery implements Serializable {

    /**
     * 部件组成明细ID
     */
    @ApiModelProperty(name="platePartsDetId",value = "部件组成明细ID")
    private Long platePartsDetId;

    /**
     * 部件组成ID
     */
    @ApiModelProperty(name="platePartsId",value = "部件组成ID")
    private Long platePartsId;

    /**
     * 部件资料名称
     */
    @ApiModelProperty(name="partsInformationName",value = "部件资料名称")
    private String partsInformationName;

    /**
     * 部件资料编码
     */
    @ApiModelProperty(name="partsInformationCode",value = "部件资料名称")
    private String partsInformationCode;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName",value = "工艺路线名称")
    private String routeName;


    private static final long serialVersionUID = 1L;
}
