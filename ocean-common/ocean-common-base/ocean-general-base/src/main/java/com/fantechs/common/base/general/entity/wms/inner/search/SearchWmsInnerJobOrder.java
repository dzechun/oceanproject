package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerJobOrder extends BaseQuery implements Serializable {
    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "作业单号")
    private String jobOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型")
    private Long orderTypeId;

    /**
     * 货主
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 工作人员
     */
    @ApiModelProperty(name="workerName",value = "工作人员")
    private String workerName;

    private Long jobOrderId;

    /**
     * 作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)
     */
    @ApiModelProperty("作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)")
    private Byte jobOrderType;

    private Long sourceOrderId;

    private Boolean isPick;

    private List<Byte> orderStatusList;
}
