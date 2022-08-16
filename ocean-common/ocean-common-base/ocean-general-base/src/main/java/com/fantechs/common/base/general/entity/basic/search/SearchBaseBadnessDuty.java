package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
public class SearchBaseBadnessDuty extends BaseQuery implements Serializable {

    /**
     * 不良责任代码
     */
    @ApiModelProperty(name="badnessDutyCode",value = "不良责任代码")
    private String badnessDutyCode;

    /**
     * 不良责任描述
     */
    @ApiModelProperty(name="badnessDutyDesc",value = "不良责任描述")
    private String badnessDutyDesc;


}
