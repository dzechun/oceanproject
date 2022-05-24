package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.dto.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class SearchWmsInnerJobOrder extends BaseQuery implements Serializable {
    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "作业单号")
    private String jobOrderCode;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据类型编码")
    private String orderTypeCode;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    private String sourceSysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    private Byte sourceBigType;

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
     * 工作人员ID
     */
    @ApiModelProperty(name="workerId",value = "工作人员ID")
    private String workerId;

    /**
     * 作业类型(1-上架 2-拣货 3-移位)
     */
    @ApiModelProperty("作业类型(1-上架 2-拣货 3-移位)")
    private Byte jobOrderType;

    /**
     * 单据状态(1-待分配 2-分配中 3-待作业 4-作业中 5-完成 6-待激活)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配 2-分配中 3-待作业 4-作业中 5-完成 6-待激活)")
    private Byte orderStatus;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "codeQueryMark",value = "查询方式标记")
    private Integer codeQueryMark;

    @ApiModelProperty(name = "query",value = "全表单查询条件")
    private HashMap<String, Query> query;

    private Long jobOrderId;

    private Long sourceOrderId;

    private Boolean isPick;

    private List<Byte> orderStatusList;

    private Long orgId;



}
