package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseKeyMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseKeyMaterialDto extends BaseKeyMaterial implements Serializable {

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="2")
    @Transient
    private String productModelCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialCode;

    /**
     * 产品料号描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialDesc;

    /**
     * 产品料号版本
     */
    @ApiModelProperty(name="version" ,value="产品料号版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="5")
    @Transient
    private String version;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    @Excel(name = "零件料号", height = 20, width = 30,orderNum="6")
    @Transient
    private String partMaterialCode;

    /**
     * 零件描述
     */
    @ApiModelProperty(name="partMaterialDesc" ,value="零件描述")
    @Excel(name = "零件描述", height = 20, width = 30,orderNum="7")
    @Transient
    private String partMaterialDesc;

    /**
     * 零件料号版本
     */
    @ApiModelProperty(name="partMaterialVersion" ,value="零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30,orderNum="8")
    @Transient
    private String partMaterialVersion;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="10")
    @Transient
    private String processName;

    /**
     * 工位名称
     */
    @ApiModelProperty(name = "stationName",value = "工位名称")
    @Excel(name = "工位名称", height = 20, width = 30,orderNum="11")
    @Transient
    private String stationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="14")
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
