package com.fantechs.common.base.general.entity.wms.inner.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/5/28
 */
@Data
public class SearchWmsInnerStockOrderDet extends BaseQuery implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "stockOrderId",value = "id")
    private Long stockOrderId;

    /**
     * 储位
     */
    @ApiModelProperty(name = "storageName",value = "储位")
    private String storageName;

    @ApiModelProperty(name = "库位编码",value = "storageCode")
    private String storageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(name = "batchCode",value = "批次号")
    private String batchCode;

    /**
     * 工作人员
     */
    @ApiModelProperty(name = "workName",value = "工作人员")
    private String workName;

    /**
     * 是否库位查询明细
     */
    @ApiModelProperty(name = "isRecords",value = "是否库位查询明细")
    private String isRecords;

    /**
     * 是否已登记(0-否 1-是)
     */
    @ApiModelProperty(name="ifRegister",value = "是否已登记(0-否 1-是)")
    private Byte ifRegister;

    /**
     * 计划类型 盘点类型(1-物料 2-库位 3-全盘)
     */
    @ApiModelProperty("计划类型")
    private Byte stockType;

    @ApiModelProperty("库位集合")
    private List<Long> stockIds;
}
