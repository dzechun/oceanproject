package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItemDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class BaseInspectionItemDetDto extends BaseInspectionItemDet implements Serializable {

    /**
     * 检验项目单号
     */
    @Transient
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    @Excel(name = "检验项目单号", height = 20, width = 30,orderNum="1")
    private String inspectionItemCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="3")
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="4")
    private String materialVersion;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
