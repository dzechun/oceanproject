package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


@Data
public class QmsFirstInspectionDto extends QmsFirstInspection implements Serializable {

    /**
     * 生产工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "生产工单号")
    @Excel(name = "生产工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name = "productCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="3")
    private String productCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "productDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="4")
    private String productDesc;

    /**
     * 物料版本
     */
    @Transient
    @ApiModelProperty(name = "version",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="5")
    private String version;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "线别名称")
    @Excel(name = "线别名称", height = 20, width = 30,orderNum="6")
    private String proName;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name = "workOrderQuantity",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="7")
    private String workOrderQuantity;


    /**
     * 处理人名称
     */
    @Transient
    @ApiModelProperty(name = "handler",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="10")
    private String handlerName;

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

    /**
     * 组织代码
     */
    @Transient
    @ApiModelProperty(name = "organizationCode",value = "组织代码")
    private String organizationCode;

    private static final long serialVersionUID = 1L;
}
