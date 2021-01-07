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
    @Excel(name = "生产工单号", height = 20, width = 30,orderNum="13")
    private String workOrderCode;

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name = "productCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="13")
    private String productCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "productDes",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="13")
    private String productDes;

    /**
     * 产品型号名称
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号名称")
    @Excel(name = "产品型号名称", height = 20, width = 30,orderNum="13")
    private String productModelName;

    /**
     * 挑选总数量
     */
    @Transient
    @ApiModelProperty(name = "selectedTotalQuantity",value = "挑选总数量")
    @Excel(name = "挑选总数量", height = 20, width = 30,orderNum="13")
    private String selectedTotalQuantity;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "线别名称")
    @Excel(name = "线别名称", height = 20, width = 30,orderNum="13")
    private String proName;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name = "workOrderQuantity",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="13")
    private String workOrderQuantity;


    /**
     * 处理人名称
     */
    @Transient
    @ApiModelProperty(name = "handler",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="13")
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

    private static final long serialVersionUID = 1L;
}
