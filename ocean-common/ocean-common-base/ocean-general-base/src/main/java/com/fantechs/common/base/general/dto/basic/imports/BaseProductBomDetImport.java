package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseProductBomDetImport implements Serializable {

    //雷赛临时数据导入

    /**
     * 凭证类型
     */
    @ApiModelProperty(name = "BSART", value = "凭证类型")
    @Excel(name = "凭证类型", height = 20, width = 30)
    private String BSART;

    /**
     * 采购凭证
     */
    @ApiModelProperty(name="EBELN" ,value="采购凭证")
    @Excel(name = "采购凭证", height = 20, width = 30)
    private String EBELN;

    /**
     * 采购凭证项目号
     */
    @ApiModelProperty(name="EBELP" ,value="采购凭证项目号")
    @Excel(name = "采购凭证项目号", height = 20, width = 30)
    private String EBELP;

    /**
     * 项目类别
     */
    @ApiModelProperty(name="EPSTP" ,value="项目类别")
    @Excel(name = "项目类别", height = 20, width = 30)
    private String EPSTP;

    /**
     * 供应商代码
     */
    @ApiModelProperty(name="LIFNR" ,value="供应商代码")
    @Excel(name = "供应商代码", height = 20, width = 30)
    private String LIFNR;

    /**
     * 创建日期
     */
    @ApiModelProperty(name="AEDAT" ,value="创建日期")
    @Excel(name = "创建日期", height = 20, width = 30,exportFormat ="yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    private Date AEDAT;

    /**
     * 物料号
     */
    @ApiModelProperty(name="MATNR" ,value="物料号")
    @Excel(name = "物料号", height = 20, width = 30)
    private String MATNR;

    /**
     * 采购订单数量
     */
    @ApiModelProperty(name="MENGE" ,value="采购订单数量")
    @Excel(name = "采购订单数量", height = 20, width = 30)
    private String MENGE;

    /**
     * 订单单位
     */
    @ApiModelProperty(name="MEINS" ,value="订单单位")
    @Excel(name = "订单单位", height = 20, width = 30)
    private String MEINS;

    /**
     * 工厂
     */
    @ApiModelProperty(name="WERKS" ,value="工厂")
    @Excel(name = "工厂", height = 20, width = 30)
    private Long WERKS;

    /**
     * 库存地点
     */
    @ApiModelProperty(name="LGORT" ,value="库存地点")
    @Excel(name = "库存地点", height = 20, width = 30)
    private String LGORT;

}
