package com.fantechs.common.base.general.entity.qms.search;

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
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 抽样方案
 * @date 2020-12-23 14:51:47
 */
@Data
public class SearchQmsSamplingPlan extends BaseQuery implements Serializable {

    /**
     * 批量
     */
    @ApiModelProperty(name="batch",value = "批量")
    private BigDecimal batch;


    /**
     * 水准（1、I  2、II  3、III）
     */
    @ApiModelProperty(name="level",value = "水准（1、I  2、II  3、III）")
    private Byte level;


    /**
     * AQL
     */
    @ApiModelProperty(name="aql",value = "AQL")
    private BigDecimal aql;

    private static final long serialVersionUID = 1L;
}
