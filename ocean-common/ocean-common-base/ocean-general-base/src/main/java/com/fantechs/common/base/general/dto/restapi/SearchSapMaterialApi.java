package com.fantechs.common.base.general.dto.restapi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchSapMaterialApi implements Serializable {

    /**
     * 开始时间
     */
    @ApiModelProperty(name="startTime" ,value="开始时间(yyyyMMdd)")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="endTime" ,value="结束时间(yyyyMMdd)")
    private String endTime;
}
