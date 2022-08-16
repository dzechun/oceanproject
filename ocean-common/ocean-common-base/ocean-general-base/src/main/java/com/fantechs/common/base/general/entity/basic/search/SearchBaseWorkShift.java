package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseWorkShift extends BaseQuery implements Serializable {

    /**
     * 班次编码
     */
    @ApiModelProperty(name="workShiftCode",value = "班次编码")
    @Excel(name = "班次编码", height = 20, width = 30)
    @Column(name = "work_shift_code")
    private String workShiftCode;

    /**
     * 班次名称
     */
    @ApiModelProperty(name="workShiftName",value = "班次名称")
    @Excel(name = "班次名称", height = 20, width = 30)
    @Column(name = "work_shift_name")
    private String workShiftName;

    /**
     * 班次描述
     */
    @ApiModelProperty(name="workShiftDesc",value = "班次描述")
    @Excel(name = "班次描述", height = 20, width = 30)
    @Column(name = "work_shift_desc")
    private String workShiftDesc;

    /**
     * 班次ID
     */
    @ApiModelProperty(name="workShiftId",value = "班次ID")
    private Long workShiftId;
}
