package com.fantechs.common.base.general.entity.basic.search;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


@Data
public class SearchBaseWarning extends BaseQuery {

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    private Long warningId;

    /**
     * 预警类型
     */
    @ApiModelProperty(name="warningType",value = "预警类型")
    private Long warningType;

    /**
     * 人员等级（0、一级人员 1、二级人员 2、三级人员）
     */
    @ApiModelProperty(name="personnelLevel",value = "人员等级（0、一级人员 1、二级人员 2、三级人员）")
    private Byte personnelLevel;

    /**
     * 通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name="notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    private Byte notificationMethod;

}
