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
    @ApiModelProperty(name = "发运批次",value = "despatchBatch")
    private String despatchBatch;

    /**
     *专业
     */
    @ApiModelProperty(name = "专业",value = "professionName")
    private String professionName;

    /**
     *合同号
     */
    @ApiModelProperty(name = "合同号",value = "contractCode")
    private String contractCode;

    /**
     *请购单号
     */
    @ApiModelProperty(name = "请购单号",value = "purchaseReqOrderCode")
    private String purchaseReqOrderCode;

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
     *材料名称
     */
    @ApiModelProperty(name = "材料名称",value = "materialName")
    private String materialName;

    /**
     *规格
     */
    @ApiModelProperty(name = "规格",value = "spec")
    private String spec;

    /**
     *主项号
     */
    @ApiModelProperty(name = "主项号",value = "dominantTermCode")
    private String dominantTermCode;

    /**
     *装置号
     */
    @ApiModelProperty(name = "装置号",value = "deviceCode")
    private String deviceCode;

    /**
     *到货量
     */
    @ApiModelProperty(name = "到货量",value = "putawayQty")
    private BigDecimal putawayQty;

    /**
     *计量单位
     */
    @ApiModelProperty(name = "计量单位",value = "unitName")
    private String unitName;

    /**
     *材料用途
     */
    @ApiModelProperty(name = "材料用途",value = "materialPurpose")
    private String materialPurpose;

    /**
     *供应商
     */
    @ApiModelProperty(name = "供应商",value = "supplierName")
    private String supplierName;
}
