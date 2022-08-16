package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseProductYield;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by lfz on 2020/9/1.
 */
@Data
public class BaseProductYieldDto extends BaseProductYield implements Serializable {

    private static final long serialVersionUID = 6786905156525741604L;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="2")
    private String proName;


    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="4")
    private String materialName;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="5")
    private String processName;

    /**
     * 产线编码
     */
    @ApiModelProperty(name="proCode",value = "产线名称")
    private String proCode;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    private String processCode;
}
