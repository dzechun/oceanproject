package com.fantechs.common.base.entity.security;

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

@Table(name = "sys_spec_item")
@Data
public class SysSpecItem extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -4072661553411752786L;
    /**
     * 配置项ID
     */
    @Id
    @Column(name = "spec_id")
    @ApiModelProperty(name="specId" ,value="配置项ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NotNull(groups= update.class,message = "配置项Id不能为空")
    private Long specId;

    /**
     * 配置所属菜单ID
     */
    @Column(name = "menu_id")
    @ApiModelProperty(name="menuId" ,value="配置所属菜单ID")
    private Long menuId;

    /**
     * 配置项代码
     */
    @Column(name = "spec_code")
    @ApiModelProperty(name="specCode" ,value="配置项代码")
    @Excel(name = "配置项代码", height = 20, width = 30)
    @NotBlank(message = "配置项代码不能为空")
    private String specCode;

    /**
     * 配置项名称
     */
    @Column(name = "spec_name")
    @ApiModelProperty(name="specName" ,value="配置项名称")
    @Excel(name = "配置项名称", height = 20, width = 30)
    @NotBlank(message = "配置项名称不能为空")
    private String specName;

    /**
     * 参数
     */
    @Column(name = "para")
    @ApiModelProperty(name="para" ,value="参数")
    @Excel(name = "参数", height = 20, width = 30)
    @NotBlank(message = "配置项参数不能为空")
    private String para;

    /**
     * 参数值
     */
    @Column(name = "para_value")
    @ApiModelProperty(name="paraValue" ,value="参数值")
    @Excel(name = "参数值", height = 20, width = 30)
    private String paraValue;

    /**
     * 参数类型
     */
    @Column(name = "param_type")
    @ApiModelProperty(name="paramType" ,value="参数类型")
    private Byte paramType;

    /**
     * 配置类别（0-系统配置，1-模块配置）默认0
     */
    @Column(name = "category")
    @ApiModelProperty(name="category" ,value="配置类别")
    @Excel(name = "配置类别", height = 20, width = 30)
    private Byte category;

    /**
     * 配置顺序
     */
    @Column(name = "order_num")
    @ApiModelProperty(name="orderNum" ,value="配置顺序")
    @Excel(name = "配置顺序", height = 20, width = 30)
    private Byte orderNum;

    /**
     * 是否隐藏(0、是 1、否 )
     */
    @Column(name = "is_hide")
    @ApiModelProperty(name="isHide" ,value="是否隐藏(0、是 1、否 )")
    private Byte isHide;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    @Excel(name = "创建账号", height = 20, width = 30)
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Transient
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
    @Excel(name = "修改账号", height = 20, width = 30)
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Transient
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1" ,value="扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2" ,value="扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3" ,value="扩展字段3")
    private String option3;

}
