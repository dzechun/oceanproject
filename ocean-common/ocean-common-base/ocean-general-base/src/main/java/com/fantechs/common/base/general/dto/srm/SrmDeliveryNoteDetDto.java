package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
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
public class SrmDeliveryNoteDetDto extends SrmDeliveryNoteDet implements Serializable {


    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    @Column(name = "material_name")
    private String materialName;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    @Column(name = "unit")
    private String unit;

    /**
     * 收货仓库名称
     */
    @ApiModelProperty(name="storageName",value = "收货仓库名称")
    @Excel(name = "收货仓库名称", height = 20, width = 30)
    @Column(name = "storageName")
    private String storageName;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30)
    @Column(name = "plan_delivery_date")
    private Date planDeliveryDate;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
