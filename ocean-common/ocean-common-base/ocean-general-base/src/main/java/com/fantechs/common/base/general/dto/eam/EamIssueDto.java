package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.general.entity.eam.EamNews;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamIssueDto extends EamIssue implements Serializable {

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
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="5")
    private String materialName;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="3")
    private String productModelName;

}
