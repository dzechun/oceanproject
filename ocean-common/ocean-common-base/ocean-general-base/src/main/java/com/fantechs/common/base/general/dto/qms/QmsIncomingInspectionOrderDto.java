package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class QmsIncomingInspectionOrderDto extends QmsIncomingInspectionOrder implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="17",needMerge = true)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="19",needMerge = true)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4",needMerge = true)
    private String materialCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="5",needMerge = true)
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="6",needMerge = true)
    private String materialVersion;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="7",needMerge = true)
    private String productModelName;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="8",needMerge = true)
    private String supplierName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="9",needMerge = true)
    private String warehouseName;

    /**
     * 检验方式
     */
    @Transient
    @ApiModelProperty(name = "inspectionWayDesc",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="11",needMerge = true)
    private String inspectionWayDesc;

    /**
     * 检验标准
     */
    @Transient
    @ApiModelProperty(name = "inspectionStandardName",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="12",needMerge = true)
    private String inspectionStandardName;

    /**
     * 附件URL
     */
    @Transient
    @ApiModelProperty(name = "accessUrl",value = "附件URL")
    @Excel(name = "附件URL", height = 20, width = 30,orderNum="16",needMerge = true)
    private String accessUrl;

    /**
     * 来料检验单明细
     */
    @ApiModelProperty(name="list",value = "来料检验单明细")
    @ExcelCollection(name="来料检验单明细",orderNum="21")
    private List<QmsIncomingInspectionOrderDet> list = new ArrayList<>();
}