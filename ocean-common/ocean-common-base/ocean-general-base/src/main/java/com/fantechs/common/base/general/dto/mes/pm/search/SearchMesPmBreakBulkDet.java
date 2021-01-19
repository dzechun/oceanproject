package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/1/18
 */
@Data
public class SearchMesPmBreakBulkDet extends BaseQuery implements Serializable {
    /**
     * 拆批作业id
     */
    @ApiModelProperty(name="breakBulkId",value = "拆批作业id")
    private Long breakBulkId;

    /**
     * 母批次号
     */
    @ApiModelProperty(name = "batchNo",value = "母批次号")
    private String batchNo;

    /**
     * 子批次号
     */
    @ApiModelProperty(name = "childLotNo",value = "子批次号")
    private String childLotNo;
}
