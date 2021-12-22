package com.fantechs.provider.lizi.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.provider.lizi.entity.LiziScanBarcodeLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/16
 */
@Data
public class LiziScanBarcodeLogDto extends LiziScanBarcodeLog implements Serializable {

    @ApiModelProperty(name = "materialDesc",value = "贴纸规格")
    @Excel(name = "贴纸规格", height = 20, width = 30,orderNum="3")
    private String materialDesc;

    @ApiModelProperty(name = "idNumber",value = "ID码")
    @Excel(name = "ID码", height = 20, width = 30,orderNum="4")
    private String idNumber;

    @ApiModelProperty(name = "skuCode",value ="SKU码" )
    @Excel(name = "SKU码", height = 20, width = 30,orderNum="5")
    private String skuCode;

    @ApiModelProperty(name = "sixNineCode",value ="69码" )
    @Excel(name = "69码", height = 20, width = 30,orderNum="6")
    private String sixNineCode;

    @ApiModelProperty(name = "createUserName",value ="创建人" )
    @Excel(name = "创建人", height = 20, width = 30,orderNum="8")
    private String createUserName;

    @ApiModelProperty(name = "modifiedUserName",value ="修改人" )
    private String modifiedUserName;

}
