package com.fantechs.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Data
public class SearchPackingOutbound extends BaseQuery implements Serializable {
    /**
     *专业
     */
    @ApiModelProperty(name = "professionName",value = "专业")
    private String professionName;

    /**
     *领料单号
     */
    @ApiModelProperty(name = "deliveryOrderCode",value = "领料单号")
    private String deliveryOrderCode;

    /**
     *领料单位
     */
    @ApiModelProperty(name = "customerName",value = "领料单位")
    private String customerName;

    /**
     *材料编码
     */
    @ApiModelProperty(name = "materialCode",value = "材料编码")
    private String materialCode;

    /**
     *位号
     */
    @ApiModelProperty(name = "locationNum",value = "位号")
    private String locationNum;

    /**
     *装置号
     */
    @ApiModelProperty(name = "deviceCode",value = "装置号")
    private String deviceCode;

    /**
     *申请量
     */
    @ApiModelProperty(name = "packingQty",value = "申请量")
    private BigDecimal packingQty;

    /**
     *批准量
     */
    @ApiModelProperty(name = "pickingQty",value = "批准量")
    private BigDecimal pickingQty;

    /**
     *实发量
     */
    @ApiModelProperty(name = "dispatchQty",value = "实发量")
    private BigDecimal dispatchQty;

    /**
     *管线号
     */
    @ApiModelProperty(name = "pipelineNumber",value = "管线号")
    private String pipelineNumber;

    /**
     *规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    private String spec;

    /**
     *材料名称
     */
    @ApiModelProperty(name = "materialName",value = "材料名称")
    private String materialName;

    /**
     *领料人
     */
    @ApiModelProperty(name = "pickMaterialUserName",value = "领料人")
    private String pickMaterialUserName;

    /**
     *审批人
     */
    @ApiModelProperty(name = "auditUserName",value = "审批人")
    private String auditUserName;

    /**
     *发料人
     */
    @ApiModelProperty(name = "issueUserName",value = "发料人")
    private String  issueUserName;
}
