package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_warehouse")
@Data
@Validated
public class BaseWarehouse extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 3791280545481754788L;
    /**
     * 仓库ID
     */
    @Id
    @Column(name = "warehouse_id")
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    @NotNull(groups = update.class,message = "仓库id不能为空")
    private Long warehouseId;

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code")
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30)
    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name")
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @Column(name = "warehouse_desc")
    @ApiModelProperty(name = "warehouseDesc",value = "仓库描述")
    @Excel(name = "仓库描述", height = 20, width = 30)
    private String warehouseDesc;

    /**
     * 仓库类型
     */
    @ApiModelProperty(name="warehouseCategory",value = "仓库类型")
    @Excel(name = "仓库类型", height = 20, width = 30,replace = {"未知类型_0","普通仓_1", "辅料仓_2","暂存仓_3","成品仓_4","原材料仓_5","线边仓_6"})
    @Column(name = "warehouse_category")
    private Long warehouseCategory;

    /**
     * 容量
     */
    @ApiModelProperty(name="capacity",value = "容量")
    @Excel(name = "容量", height = 20, width = 30)
    private BigDecimal capacity;

    /**
     * 温度
     */
    @ApiModelProperty(name="temperature",value = "温度")
    @Excel(name = "温度", height = 20, width = 30)
    private BigDecimal temperature;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 是否参与齐套分析(0.否 1.是)
     */
    @ApiModelProperty(name="completeAnalysis",value = "是否参与齐套分析(0.否 1.是)")
    @Column(name = "complete_analysis")
    @Excel(name = "是否参与齐套分析", height = 20, width = 30,replace = {"否_0", "是_1"})
    private Integer completeAnalysis;

    /**
     * 是否参与MRB运算(0.否 1.是)
     */
    @ApiModelProperty(name="MrbOperation",value = "是否参与齐套分析(0.否 1.是)")
    @Column(name = "mrb_operation")
    @Excel(name = "是否参与MRB运算(0.否 1.是)", height = 20, width = 30,replace = {"否_0", "是_1"})
    private Integer mrbOperation;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30)
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30)
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30)
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 地址
     */
    @ApiModelProperty(name="address",value = "地址")
    @Excel(name = "地址", height = 20, width = 30)
    @Column(name = "address")
    private String address;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除")
    private Byte isDelete;

    /**
     * 仓库货主关系集合
     */
    @ApiModelProperty(name = "baseMaterialOwnerReWhDtos",value = "仓库货主关系集合")
    private List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = new ArrayList<>();

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

}
