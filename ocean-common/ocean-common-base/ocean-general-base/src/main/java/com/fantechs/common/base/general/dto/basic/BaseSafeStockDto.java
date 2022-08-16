package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/4
 */
@Data
public class BaseSafeStockDto extends BaseSafeStock implements Serializable {
    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name")
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "1")
    private String warehouseName;

    /**
     * 货主名称
     */
    @Column(name = "material_owner_name")
    @ApiModelProperty(name = "materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30)
    private String materialOwnerName;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(name="materialCategoryName",value = "物料类别名称")
    @Excel(name = "物料类别名称", height = 20, width = 30,orderNum="2")
    @Column(name = "material_category_name")
    private String materialCategoryName;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="3")
    private String materialCode;

    /**
     * 物料名称
     */
    @Column(name = "material_name")
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="4")
    private String materialName;

    /**
     * 物料描述
     */
    @Column(name = "material_desc")
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum = "5")
    private String materialDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30,orderNum = "6")
    private String materialVersion;

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
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String organizationName;

    private Boolean isMax;
}
