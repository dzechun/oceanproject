package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerJobOrderDet extends BaseQuery implements Serializable {

    /**
     * 作业类型(1-上架 2-拣货 3-移位)
     */
    @ApiModelProperty("作业类型(1-上架 2-拣货 3-移位)")
    private Byte jobOrderType;

    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID")
    private Long jobOrderId;

    @ApiModelProperty(name="lineStatusList",value = "明细状态(1-待分配 2-待作业 3-已完成")
    private List<Byte> lineStatusList;

    private Long jobOrderDetId;

    @ApiModelProperty(name="nonShiftStorageStatus",value = "不等于，移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)")
    private byte nonShiftStorageStatus;

    @ApiModelProperty(name="ifFiltrate",value = "是否筛选（0，否  1，是）")
    private byte ifFiltrate;

    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name="materialDesc",value = "物料描述")
    private String materialDesc;

    @ApiModelProperty(name="materialVersion",value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(name="jobOrderCode",value = "上架单单号")
    private String jobOrderCode;

}
