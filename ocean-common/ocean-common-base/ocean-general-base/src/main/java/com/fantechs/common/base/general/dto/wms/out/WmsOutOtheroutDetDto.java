package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsOutOtheroutDetDto extends WmsOutOtheroutDet implements Serializable {

    /**
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    @Excel(name = "其他出库单号", height = 20, width = 30,orderNum="1")
    @Transient
    private String otheroutCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit" ,value="单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="5")
    private String unit;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30,orderNum="6")
    private String version;

    /**
     * 仓库名称（出货仓库）
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称（出货仓库）")
    @Excel(name = "仓库ID（出货仓库）", height = 20, width = 30,orderNum="11")
    @Column(name = "warehouse_name")
    private String warehouseName;

    /**
     * 仓库编码（出货仓库）
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码（出货仓库）")
    @Excel(name = "仓库编码（出货仓库）", height = 20, width = 30,orderNum="12")
    @Column(name = "warehouse_code")
    private String warehouseCode;

    /**
     * 仓库描述（出货仓库）
     */
    @ApiModelProperty(name="warehouseDesc",value = "仓库描述（出货仓库）")
    @Excel(name = "仓库描述（出货仓库）", height = 20, width = 30,orderNum="13")
    @Column(name = "warehouse_desc")
    private String warehouseDesc;

    /**
     * 仓库管理员名称
     */
    @ApiModelProperty(name="warehouseUserName",value = "仓库管理员名称")
    @Excel(name = "仓库管理员名称", height = 20, width = 30,orderNum="14")
    private String warehouseUserName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="15")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="16")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
