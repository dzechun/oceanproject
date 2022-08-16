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
 * @date 2020-12-28 15:15:07
 */
@Data
public class SearchQmsRejectsMrbReview extends BaseQuery implements Serializable {

    /**
     * 评审单号
     */
    @ApiModelProperty(name="mrbReviewCode",value = "评审单号")
    private String rejectsMrbReviewCode;

    /**
     * 状态（0、审核中 1、已审核 2、未审核）
     */
    @ApiModelProperty(name="receiptsStatus",value = "状态（0、审核中 1、已审核 2、未审核）")
    private Byte receiptsStatus;

    private static final long serialVersionUID = 1L;
}
