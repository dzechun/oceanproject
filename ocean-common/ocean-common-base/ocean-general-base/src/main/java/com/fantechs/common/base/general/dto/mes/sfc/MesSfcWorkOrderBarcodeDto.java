package com.fantechs.common.base.general.dto.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/4/7
 */
@Data
public class MesSfcWorkOrderBarcodeDto extends MesSfcWorkOrderBarcode implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 产品ID
     */
    @Transient
    @ApiModelProperty(name="materialId",value = "产品ID")
    private Long materialId;

    private String materialName;

    /**
     * 条码类别名称
     */
    @Transient
    @ApiModelProperty(name = "labelCategoryName",value = "条码类别")
    private String labelCategoryName;

    @Transient
    @ApiModelProperty(name = "samePackageCode",value = "PO号")
    private String samePackageCode;

    @Transient
    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @Transient
    @ApiModelProperty(name = "cutsomerBarcode",value = "客户条码")
    private String cutsomerBarcode;

    @Transient
    @ApiModelProperty(name = "reprintCount",value = "补打次数")
    private String reprintCount;
}
