package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/1/18
 */
@Data
public class SearchMesPmBreakBulk extends BaseQuery implements Serializable {
    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    @ApiModelProperty(name = "产品料号")
    private String materialCode;

    /**
     * 作业类型
     */
    @ApiModelProperty(name = "breakBulkType",value = "作业类型(1、拆批作业，2、合批作业)")
    @NotBlank
    private Byte breakBulkType;

    @ApiModelProperty(name = "workOrderId",value = "工单id")
    private Long workOrderId;
}
