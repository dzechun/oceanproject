package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_work_shop")
@Data
public class BaseWorkShop extends ValidGroup implements Serializable{
    private static final long serialVersionUID = 5732515197537200878L;
    /**
     * id
     */
    @Id
    @Column(name = "work_shop_id")
    @ApiModelProperty(name = "workShopId",value = "车间id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = update.class,message = "车间id不能为空")
    private Long workShopId;

    /**
     * 车间编码
     */
    @Column(name = "work_shop_code")
    @Excel(name = "车间编码", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    @NotBlank(message = "车间编码不能为空")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Column(name = "work_shop_name")
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    @NotBlank(message = "车间名称不能为空")
    private String workShopName;

    /**
     * 车间描述
     */
    @Column(name = "work_shop_desc")
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    @Excel(name = "车间描述", height = 20, width = 30,orderNum="3")
    private String workShopDesc;

    /**
     * 工厂id
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name = "factoryId",value = "工厂id")
    @NotNull(message = "工厂id不能为空")
    private Long factoryId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建账号id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改账号id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 车间状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name = "status",value = "车间状态（0、不启用 1、启用）")
    @Excel(name = "车间状态", height = 20, width = 30 ,orderNum="6",replace = {"不启用_0", "启用_1"})
    private Integer status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

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
