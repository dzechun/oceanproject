package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesScheduleDetailDTO extends MesScheduleDetail implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(value = "工单号",example = "工单号")
    @Excel(name = "工单号")
    private String workOrderCode;
    /**
     * 生产订单号
     */
    @Transient
    @ApiModelProperty(value = "生产订单号",example = "生产订单号")
    @Excel(name = "生产订单号")
    private String orderCode;
    /**
     * 合同号
     */
    @Transient
    @ApiModelProperty(value = "合同号",example = "合同号")
    @Excel(name = "合同号")
    private String contactCode;
    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(value = "产品料号",example = "产品料号")
    @Excel(name = "产品料号")
    private String materialCode;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(value = "产品型号",example = "产品型号")
    @Excel(name = "产品型号")
    private String productModelName;
    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(value = "产品版本",example = "产品版本")
    @Excel(name = "产品版本")
    private String version;
    /**
     * 产品数量
     */
    @Transient
    @ApiModelProperty(value = "产品数量",example = "产品数量")
    @Excel(name = "产品数量")
    private BigDecimal workOrderQuantity;
    /**
     * 完工数量
     */
    @Transient
    @ApiModelProperty(value = "完工数量",example = "完工数量")
    @Excel(name = "完工数量")
    private BigDecimal outputQuantity;
    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(value = "产品描述",example = "产品描述")
    @Excel(name = "产品描述")
    private String materialDesc;
}