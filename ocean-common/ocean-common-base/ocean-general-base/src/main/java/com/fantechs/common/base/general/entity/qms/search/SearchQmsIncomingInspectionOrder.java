package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchQmsIncomingInspectionOrder extends BaseQuery implements Serializable {
    /**
     * 来料检验单id
     */
    @ApiModelProperty(name = "incomingInspectionOrderId", value = "来料检验单id")
    private Long incomingInspectionOrderId;

    /**
     * 来料检验单号
     */
    @ApiModelProperty(name = "incomingInspectionOrderCode", value = "来料检验单号")
    private String incomingInspectionOrderCode;

    /**
     * 来源单号
     */
    @ApiModelProperty(name = "sourceOrderCode", value = "来源单号")
    private String sourceOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name = "materialCode", value = "产品料号")
    private String materialCode;

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

    /**
     * MRB评审(1-特采 2-挑选使用 3-退供应商)
     */
    @ApiModelProperty(name="mrbResult",value = "MRB评审(1-特采 2-挑选使用 3-退供应商)")
    private Byte mrbResult;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    @ApiModelProperty(name="ifFiltrate",value = "是否筛选（0，否  1，是）")
    private byte ifFiltrate;

}
