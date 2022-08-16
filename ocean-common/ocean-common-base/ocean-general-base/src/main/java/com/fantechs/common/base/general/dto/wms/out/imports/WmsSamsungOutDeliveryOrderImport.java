package com.fantechs.common.base.general.dto.wms.out.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsSamsungOutDeliveryOrderImport implements Serializable {

    /**
     * 装箱计划时间(yyyy-MM-dd)
     */
    @Excel(name = "装箱计划时间(yyyy-MM-dd)",  height = 20, width = 30,importFormat = "yyyy-MM-dd")
    @ApiModelProperty(name="planDespatchDate" ,value="装箱计划时间(yyyy-MM-dd)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDespatchDate;

    //-------------明细----------------

    /**
     * LID号(必填)
     */
    @Excel(name = "LID号(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="option1" ,value="LID号(必填)")
    private String option1;

    /**
     * 万宝PO号
     */
    @ApiModelProperty(name="option3",value = "万宝PO号")
    @Excel(name = "万宝PO号",  height = 20, width = 30)
    private String option3;

    /**
     * BR号
     */
    @ApiModelProperty(name="option2",value = "BR号")
    @Excel(name = "BR号",  height = 20, width = 30)
    private String option2;

    /**
     * 物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

    /**
     * 包装单位
     */
    @ApiModelProperty(name = "packingUnitName",value = "包装单位")
    @Excel(name = "包装单位", height = 20, width = 30)
    private String packingUnitName;

    /**
     * 包装数量
     */
    @ApiModelProperty(name = "packingQty",value = "包装数量")
    @Excel(name = "包装数量", height = 20, width = 30)
    private BigDecimal packingQty;


}
