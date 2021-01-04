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
public class SearchBaseWorkShiftTime extends BaseQuery implements Serializable {

    /**
     * 班次id
     */
    @ApiModelProperty(name="workShiftId",value = "班次id")
    private Long workShiftId;

    private static final long serialVersionUID = 1L;
}
