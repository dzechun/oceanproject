package com.fantechs.common.base.general.entity.basic.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "base_ht_work_shop")
@Data
public class BaseHtWorkShop implements Serializable {
    private static final long serialVersionUID = 1843661571608472948L;
    /**
     * id
     */
    @Id
    @Column(name = "ht_work_shop_id")
    private Long htWorkShopId;

    @Column(name = "work_shop_id")
    @ApiModelProperty(name = "workShopId",value = "车间id")
    private Long workShopId;

    /**
     * 车间编码
     */
    @Column(name = "work_shop_code")
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Column(name = "work_shop_name")
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 车间描述
     */
    @Column(name = "work_shop_desc")
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    private String workShopDesc;

    /**
     * 工厂id
     */
    @Column(name = "factory_id")
    @ApiModelProperty(name = "factoryId",value = "工厂id")
    private Long factoryId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 车间状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name = "status",value = "车间状态（0、不启用 1、启用）")
    private Integer status;

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
    private String modifiedUserName;

}
