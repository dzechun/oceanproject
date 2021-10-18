package com.fantechs.common.base.general.dto.esop;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EsopIssueDto extends EsopIssue implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "物料名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="5")
    private String materialName;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="3")
    private String productModelName;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelCode",value = "产品型号")
    private String productModelCode;

    /**
     * 产品规格
     */
    @Transient
    @ApiModelProperty(name = "productModelDesc",value = "产品规格")
    private String productModelDesc;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name = "processCode",value = "工序编码")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    private String processName;

}
