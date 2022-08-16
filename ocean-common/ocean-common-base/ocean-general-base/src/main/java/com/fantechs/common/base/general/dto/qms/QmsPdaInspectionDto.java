package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


/**
 * PDA质检
 * @date 2021-01-07 18:50:51
 */
@Data
public class QmsPdaInspectionDto extends QmsPdaInspection implements Serializable {

    /**
     * 栈板码
     */
    @Transient
    @ApiModelProperty(name = "palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum = "3")
    private String palletCode;

    /**
     * 生产工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    @Excel(name = "生产工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;


    /**
     * 生产线
     */
    @Transient
    @ApiModelProperty(name="productionLine",value = "生产线")
    @Excel(name = "生产线", height = 20, width = 30,orderNum="4")
    private String  productionLine;

    /**
     * 处理人名称
     */
    @Transient
    @ApiModelProperty(name="handlerName",value = "处理人名称")
    @Excel(name = "处理人名称", height = 20, width = 30,orderNum="6")
    private String handlerName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "反馈人", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 组织代码
     */
    @Transient
    @ApiModelProperty(name = "organizationCode",value = "组织代码")
    @Excel(name = "组织代码", height = 20, width = 30,orderNum = "0")
    private String organizationCode;

    /**
     * 合格状态
     */
    @Excel(name = "合格状态", height = 20, width = 30,orderNum = "9")
    private String qualifiedState;

    private static final long serialVersionUID = 1L;
}
