package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
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
     * 仓库
     */
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 工作人员
     */
    @ApiModelProperty(name="workName",value = "工作人员")
    private String workName;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "codeQueryMark",value = "查询方式标记")
    private Integer codeQueryMark;

    private Long jobOrderId;

    /**
     * 作业类型(1-上架 2-拣货 3-移位)
     */
    @ApiModelProperty("作业类型(1-上架 2-拣货 3-移位)")
    private Byte jobOrderType;

    private Long sourceOrderId;

    private Boolean isPick;

    private List<Byte> orderStatusList;

    private Long orgId;

    /**
     * 单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)")
    private Byte orderStatus;

}
