package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchQmsIpqcInspectionOrder extends BaseQuery implements Serializable {

    /**
     * IPQC检验单id
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单id")
    private Long ipqcInspectionOrderId;

    /**
     * IPQC检验单号
     */
    @ApiModelProperty(name="ipqcInspectionOrderCode",value = "IPQC检验单号")
    private String ipqcInspectionOrderCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 检验状态
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态")
    private Byte inspectionStatus;

    /**
     * 检验状态集合(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatusList",value = "检验状态集合(1-待检验 2-检验中 3-已检验)")
    private List<Byte> inspectionStatusList;

    /**
     * 检验结果
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果")
    private Byte inspectionResult;

    /**
     * 检验方式描述
     */
    @ApiModelProperty(name="inspectionWayDesc" ,value="检验方式描述")
    private String inspectionWayDesc;

    /**
     * 检验方式编码
     */
    @ApiModelProperty(name="inspectionWayCode" ,value="检验方式编码")
    private String inspectionWayCode;

    /**
     * 流水号
     */
    @ApiModelProperty(name="barcodes" ,value="流水号")
    private String barcodes;
}
