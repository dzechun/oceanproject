package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_work_shop")
@Data
public class SmtWorkShop  implements Serializable{
    private static final long serialVersionUID = 5732515197537200878L;
    /**
     * id
     */
    @Id
    @Column(name = "work_shop_id")
    @ApiModelProperty(name = "workShopId",value = "车间id")
    private Long workShopId;

    /**
     * 车间编码
     */
    @Column(name = "work_shop_code")
    @Excel(name = "车间编码", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Column(name = "work_shop_name")
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name = "workShopName",value = "车间名称")
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
    private Long factoryId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "车间状状态", height = 20, width = 30 ,orderNum="5",replace = {"不启用_0", "启用_1"})
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

}