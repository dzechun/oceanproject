package com.fantechs.entity.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class SearchPackingWarehousingModel extends BaseQuery implements Serializable {
    /**
     *发运批次
     */
    @ApiModelProperty(name = "despatchBatch",value = "发运批次")
    private String despatchBatch;

    /**
     *专业
     */
    @ApiModelProperty(name = "professionName",value = "专业")
    private String professionName;

    /**
     *合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    private String contractCode;

    /**
     *请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

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
     *材料名称
     */
    @ApiModelProperty(name = "materialName",value = "材料名称")
    private String materialName;

    /**
     *规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    private String spec;

    /**
     *主项号
     */
    @ApiModelProperty(name = "dominantTermCode",value = "主项号")
    private String dominantTermCode;

    /**
     *装置号
     */
    @ApiModelProperty(name = "deviceCode",value = "装置号")
    private String deviceCode;

    /**
     *到货量
     */
    @ApiModelProperty(name = "putawayQty",value = "到货量")
    private BigDecimal putawayQty;

    /**
     *计量单位
     */
    @ApiModelProperty(name = "unitName",value = "计量单位")
    private String unitName;

    /**
     *材料用途
     */
    @ApiModelProperty(name = "materialPurpose",value = "材料用途")
    private String materialPurpose;

    /**
     *供应商
     */
    @ApiModelProperty(name = "supplierName",value = "供应商")
    private String supplierName;
}
