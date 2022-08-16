package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerBarcodeOperation;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/27
 */
@Data
public class WmsInnerBarcodeOperationDto extends WmsInnerBarcodeOperation implements Serializable {

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 物料规格
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "物料规格")
    @Excel(name = "物料规格", height = 20, width = 30)
    private String materialDesc;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30)
    private String modifiedUserName;
}
