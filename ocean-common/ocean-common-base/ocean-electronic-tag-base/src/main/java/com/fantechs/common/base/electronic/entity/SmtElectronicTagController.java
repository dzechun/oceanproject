package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 电子标签控制器表
 * smt_electronic_tag_controller
 * @author 53203
 * @date 2020-11-16 18:25:53
 */
@Data
@Table(name = "smt_electronic_tag_controller")
public class SmtElectronicTagController extends ValidGroup implements Serializable {
    /**
     * 电子标签控制器id
     */
    @ApiModelProperty(name="electronicTagControllerId",value = "电子标签控制器id")
    @Id
    @Column(name = "electronic_tag_controller_id")
    @NotNull(groups = update.class,message = "电子标签控制器id不能为空")
    private Long electronicTagControllerId;

    /**
     * 电子标签控制器编码
     */
    @ApiModelProperty(name="electronicTagControllerCode",value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码", height = 20, width = 30,orderNum="1")
    @Column(name = "electronic_tag_controller_code")
    @NotBlank(message = "电子标签控制器编码不能为空")
    private String electronicTagControllerCode;

    /**
     * 电子标签控制器名称
     */
    @ApiModelProperty(name="electronicTagControllerName",value = "电子标签控制器名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30,orderNum="2")
    @Column(name = "electronic_tag_controller_name")
    @NotBlank(message = "电子标签控制器名称不能为空")
    private String electronicTagControllerName;

    /**
     * 电子标签控制器描述
     */
    @ApiModelProperty(name="electronicTagControllerDesc",value = "电子标签控制器描述")
    @Excel(name = "电子标签控制器描述", height = 20, width = 30,orderNum="3")
    @Column(name = "electronic_tag_controller_desc")
    private String electronicTagControllerDesc;

    /**
     * 电子标签控制器ip
     */
    @ApiModelProperty(name="electronicTagControllerIp",value = "电子标签控制器ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,orderNum="4")
    @Column(name = "electronic_tag_controller_ip")
    @NotBlank(message = "电子标签控制器ip不能为空")
    private String electronicTagControllerIp;

    /**
     * 电子标签控制器端口
     */
    @ApiModelProperty(name="electronicTagControllerPort",value = "电子标签控制器端口")
    @Excel(name = "电子标签控制器端口", height = 20, width = 30,orderNum="5")
    @Column(name = "electronic_tag_controller_port")
    @NotBlank(message = "电子标签控制器端口不能为空")
    private String electronicTagControllerPort;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,orderNum="6",replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}