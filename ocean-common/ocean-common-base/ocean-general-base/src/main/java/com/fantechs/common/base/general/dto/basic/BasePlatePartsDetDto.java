package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class BasePlatePartsDetDto extends BasePlatePartsDet implements Serializable {

    /**
     * 部件资料编码
     */
    @Transient
    @ApiModelProperty(name="partsInformationCode",value = "部件资料编码")
    @Excel(name = "部件资料编码", height = 20, width = 30,orderNum="")
    private String partsInformationCode;

    /**
     * 部件资料名称
     */
    @Transient
    @ApiModelProperty(name="partsInformationName",value = "部件资料名称")
    @Excel(name = "部件资料名称", height = 20, width = 30,orderNum="")
    private String partsInformationName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName",value = "工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="")
    private String routeName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
