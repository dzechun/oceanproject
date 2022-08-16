package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
@Data
public class ProductionInStorageDet extends ProductionInStorage implements Serializable {
    /**
     * 厂内码
     */
    @ApiModelProperty(name = "inPlantBarCode",value = "厂内码")
    @Excel(name = "厂内码", height = 20, width = 30,orderNum="14")
    private String inPlantBarCode;

    /**
     *销售码
     */
    @ApiModelProperty(name = "marketBarCode",value = "销售码")
    @Excel(name = "销售码", height = 20, width = 30,orderNum="15")
    private String marketBarCode;

    /**
     * 打包时间
     */
    @ApiModelProperty(name = "packageTime",value = "打包时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "打包时间", height = 20, width = 30,orderNum="16",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date packageTime;

    /**
     * 入库下线时间
     */
    @ApiModelProperty(name = "putInStorageTime",value = "入库下线时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库下线时间", height = 20, width = 30,orderNum="17",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date putInStorageTime;
}
