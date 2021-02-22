package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchWmsOutProductionMaterial extends BaseQuery implements Serializable {

    /**
     * 生产领料单号
     */
    @ApiModelProperty(name="productionMaterialCode",value = "生产领料单号")
    private String productionMaterialCode;

    /**
     * 发料日期
     */
    @ApiModelProperty(name="outTimeStart",value = "发料日期-起")
    private String outTimeStart;

    /**
     * 发料日期
     */
    @ApiModelProperty(name="outTimeEnd",value = "发料日期-止")
    private String outTimeEnd;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proLineName",value = "产线名称")
    private String proLineName;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;





}
