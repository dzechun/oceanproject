package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class SrmInAsnOrderDto extends SrmInAsnOrder implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;


    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 供应商
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商")
    private String supplierName;

    /**
     * 单据名称
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据名称")
    private String orderTypeName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="5")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    private List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos;

    /**
     * 文件ID
     */
    @ApiModelProperty(name="fileUrl",value = "文件ID")
    private String fileUrl;


    //   private List<SrmInAsnOrderDetBarcode> srmInAsnOrderDetBarcodes;

}
