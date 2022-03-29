package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(name="workName",value = "工作人员")
    private String workName;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "codeQueryMark",value = "查询方式标记")
    private Integer codeQueryMark;

    private Long jobOrderId;

    /**
     * 作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)
     */
    @ApiModelProperty("作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)")
    private Byte jobOrderType;

    private Long sourceOrderId;

    private Boolean isPick;

    private List<Byte> orderStatusList;

    private Long orgId;

    private Long userId;

    /**
     * 是否栈板自动生成(1-是，0-否) PDA标识字段
     */
    @ApiModelProperty(name = "isPallet",value = "是否栈板自动生成(1-是，0-否) PDA标识字段")
    private Byte isPallet;

    /**
     * 单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)")
    private Byte orderStatus;

    private Byte sealOrder;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "扩展栏位")
    private String option1;

    private Long platformId;
}
