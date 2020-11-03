package com.fantechs.common.base.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtMaterialSupplierDto extends SmtMaterialSupplier implements Serializable {

    private static final long serialVersionUID = -8520931395746280600L;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum = "1")
    private String materialCode;
    /**
     * 物料版本
     */
    @Transient
    @Excel(name = "物料版本", height = 20, width = 30,orderNum = "2")
    private String version;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum = "3")
    private String materialDesc;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

}
