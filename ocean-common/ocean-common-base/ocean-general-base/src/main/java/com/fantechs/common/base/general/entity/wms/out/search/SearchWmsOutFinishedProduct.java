package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 成品出库单查询实体
 * @author hyc
 * @date 2020-12-22 15:02:48
 */
@Data
public class SearchWmsOutFinishedProduct extends BaseQuery implements Serializable {


    /**
     * 成品出库单号
     */
    @ApiModelProperty(name="finishedProductCode",value = "成品出库单号")
    private String finishedProductCode;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttimeStart",value = "预计出库时间-起")
    private Date planOuttimeStart;

    /**
     * 预计出库时间
     */
    @ApiModelProperty(name="planOuttimeEnd",value = "预计出库时间-止")
    private Date planOuttimeEnd;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttimeStart",value = "实际出库时间-起")
    private Date realityOuttimeStart;

    /**
     * 实际出库时间
     */
    @ApiModelProperty(name="realityOuttimeEnd",value = "实际出库时间-止")
    private Date realityOuttimeEnd;


    /**
     * 运输方式（0-陆运 1-海运 2-空运
     */
    @ApiModelProperty(name="trafficType",value = "运输方式（0-陆运 1-海运 2-空运")
    private Byte trafficType;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    private Byte outStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;


    private static final long serialVersionUID = 1L;
}