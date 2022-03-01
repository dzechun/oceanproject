package com.fantechs.common.base.general.entity.wanbao.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchQmsInspectionOrder extends BaseQuery implements Serializable {

    /**
     * 检验单id
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单id")
    private Long inspectionOrderId;

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "检验单号")
    private String inspectionOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 客户
     */
    @ApiModelProperty(name="customerName",value = "客户")
    private String customerName;

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    /**
     * PO号
     */
    @ApiModelProperty(name="samePackageCode",value = "PO号")
    private String samePackageCode;

    /**
     * 审批人
     */
    @ApiModelProperty(name="auditUserName",value = "审批人")
    private String auditUserName;

    /**
     * 检验人
     */
    @ApiModelProperty(name="inspectionUserName",value = "检验人")
    private String inspectionUserName;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    private Byte inspectionStatus;

    /**
     * 检验状态集合(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatusList",value = "检验状态集合(1-待检验 2-检验中 3-已检验)")
    private List<Byte> inspectionStatusList;

    /**
     * 检验结果(0-不合格 1-合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-不合格 1-合格)")
    private Byte inspectionResult;

}
