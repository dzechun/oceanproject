package com.fantechs.common.base.electronic.entity.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 电子标签控制器历史表
 * smt_ht_electronic_tag_controller
 * @author 53203
 * @date 2020-11-16 18:25:54
 */
@Data
@Table(name = "smt_ht_electronic_tag_controller")
public class SmtHtElectronicTagController implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(name="htElectronicTagControllerId",value = "id")
    @Excel(name = "id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_electronic_tag_controller_id")
    private Long htElectronicTagControllerId;

    /**
     * 标签控制器id
     */
    @ApiModelProperty(name="electronicTagControllerId",value = "标签控制器id")
    @Excel(name = "标签控制器id", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_id")
    private Long electronicTagControllerId;

    /**
     * 电子标签控制器编码
     */
    @ApiModelProperty(name="electronicTagControllerCode",value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_code")
    private String electronicTagControllerCode;

    /**
     * 电子标签控制器名称
     */
    @ApiModelProperty(name="electronicTagControllerName",value = "电子标签控制器名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_name")
    private String electronicTagControllerName;

    /**
     * 电子标签控制器描述
     */
    @ApiModelProperty(name="electronicTagControllerDesc",value = "电子标签控制器描述")
    @Excel(name = "电子标签控制器描述", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_desc")
    private String electronicTagControllerDesc;

    /**
     * 电子标签控制器ip
     */
    @ApiModelProperty(name="electronicTagControllerIp",value = "电子标签控制器ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_ip")
    private String electronicTagControllerIp;

    /**
     * 电子标签控制器端口
     */
    @ApiModelProperty(name="electronicTagControllerPort",value = "电子标签控制器端口")
    @Excel(name = "电子标签控制器端口", height = 20, width = 30,orderNum="") 
    @Column(name = "electronic_tag_controller_port")
    private String electronicTagControllerPort;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
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

    private static final long serialVersionUID = 1L;
}