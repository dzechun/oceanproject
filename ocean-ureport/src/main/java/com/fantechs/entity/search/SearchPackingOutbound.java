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
    @ApiModelProperty(name = "专业",value = "professionName")
    private String professionName;

    /**
     *领料单号
     */
    @ApiModelProperty(name = "领料单号",value = "deliveryOrderCode")
    private String deliveryOrderCode;

    /**
     *领料单位
     */
    @ApiModelProperty(name = "领料单位",value = "customerName")
    private String customerName;

    /**
     *材料编码
     */
    @ApiModelProperty(name = "材料编码",value = "materialCode")
    private String materialCode;

    /**
     *位号
     */
    @ApiModelProperty(name = "位号",value = "locationNum")
    private String locationNum;

    /**
     *装置号
     */
    @ApiModelProperty(name = "装置号",value = "deviceCode")
    private String deviceCode;

    /**
     *申请量
     */
    @ApiModelProperty(name = "申请量",value = "packingQty")
    private BigDecimal packingQty;

    /**
     *批准量
     */
    @ApiModelProperty(name = "批准量",value = "pickingQty")
    private BigDecimal pickingQty;

    /**
     *实发量
     */
    @ApiModelProperty(name = "实发量",value = "dispatchQty")
    private BigDecimal dispatchQty;

    /**
     *管线号
     */
    @ApiModelProperty(name = "管线号",value = "pipelineNumber")
    private String pipelineNumber;

    /**
     *规格
     */
    @ApiModelProperty(name = "规格",value = "spec")
    private String spec;

    /**
     *材料名称
     */
    @ApiModelProperty(name = "材料名称",value = "materialName")
    private String materialName;

    /**
     *领料人
     */
    @ApiModelProperty(name = "领料人",value = "pickMaterialUserName")
    private String pickMaterialUserName;

    /**
     *审批人
     */
    @ApiModelProperty(name = "审批人",value = "auditUserName")
    private String auditUserName;

    /**
     *发料人
     */
    @ApiModelProperty(name = "发料人",value = "issueUserName")
    private String  issueUserName;
}
