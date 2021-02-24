package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
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
public class QmsInspectionTypeDto extends QmsInspectionType implements Serializable{


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="15")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="17")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 检验类型水平名称
     */
    @Excel(name = "检验类型水平", height = 20, width = 30,orderNum="3")
    private String inspectionTypeLevelName;

    /**
     * 检验工具名称
     */
    @Excel(name = "检验工具", height = 20, width = 30,orderNum="4")
    private String inspectionToolName;

    /**
     * 检验项名称
     */
    @Excel(name = "检验项", height = 20, width = 30,orderNum="6")
    private String inspectionNapeName;

    /**
     * 测试方法
     */
    @Excel(name = "测试方法", height = 20, width = 30,orderNum="12")
    private String testMethodName;

    private static final long serialVersionUID = 1L;
}
