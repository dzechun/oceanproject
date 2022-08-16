package com.fantechs.common.base.general.entity.qms.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * PDA质检明细
 * @date 2021-01-07 20:01:55
 */
@Data
@Table(name = "qms_pda_inspection_det")
public class SearchQmsPdaInspectionDet extends BaseQuery implements Serializable {

    /**
     * PDA质检ID
     */
    @ApiModelProperty(name="pdaInspectionId",value = "PDA质检ID")
    private Long pdaInspectionId;

    /**
     * 箱码ID
     */
    @ApiModelProperty(name="packageManagerId",value = "箱码ID")
    private Long packageManagerId;



    private static final long serialVersionUID = 1L;
}
