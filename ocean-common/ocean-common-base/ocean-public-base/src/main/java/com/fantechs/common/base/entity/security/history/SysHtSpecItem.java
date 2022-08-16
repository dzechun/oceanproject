package com.fantechs.common.base.entity.security.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_ht_spec_item")
@Data
public class SysHtSpecItem implements Serializable {

    private static final long serialVersionUID = -7436037670166462777L;
    /**
     * 配置项历史ID
     */
    @Id
    @Column(name = "ht_spec_id")
    @ApiModelProperty(name="htSpecId" ,value="配置项历史ID")
    private Long htSpecId;

    /**
     * 配置项ID
     */
    @Column(name = "spec_id")
    @ApiModelProperty(name="specId" ,value="配置项ID")
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
    private String specCode;

    /**
     * 配置项名称
     */
    @Column(name = "spec_name")
    @ApiModelProperty(name="specName" ,value="配置项名称")
    private String specName;

    /**
     * 参数
     */
    @Column(name = "para")
    @ApiModelProperty(name="para" ,value="参数")
    private String para;

    /**
     * 参数值
     */
    @Column(name = "para_value")
    @ApiModelProperty(name="paraValue" ,value="参数值")
    private String paraValue;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

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
