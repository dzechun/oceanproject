package com.fantechs.common.base.dto.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtWorkOrderDto extends SmtWorkOrder implements Serializable {

    private static final long serialVersionUID = 5293503265879392765L;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="")
    private String routeName;

    /**
     * 条码规则集合ID
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleName" ,value="条码规则集合")
    private String barcodeRuleName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="")
    private String modifiedUserName;
}
