package com.fantechs.common.base.entity.sysmanage.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_ht_menuinfo")
@Data
public class SmtHtMenuInfo implements Serializable{

    private static final long serialVersionUID = -5556205534169994830L;
    /**
     * id
     */
    @Id
    @Column(name = "ht_menu_id")
    private String htMenuId;

    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private String menuId;


    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    @ApiModelProperty(name = "menuCode",value = "菜单编码")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    @ApiModelProperty(name = "menuName",value = "菜单名称")
    private String menuName;

    /**
     * 菜单顺序
     */
    @Column(name = "order_num")
    @ApiModelProperty(name = "orderNum",value = "菜单顺序")
    private Long orderNum;

    /**
     * 父级菜单id
     */
    @Column(name = "parent_id")
    @ApiModelProperty(name = "parentId",value = "父级菜单id")
    private String parentId;

    /**
     * 菜单描述
     */
    @Column(name = "menu_desc")
    @ApiModelProperty(name = "menuDesc",value = "菜单描述")
    private String menuDesc;

    /**
     * 菜单层级
     */
    @Column(name = "menu_level")
    @ApiModelProperty(name = "menuLevel",value = "菜单层级")
    private Long menuLevel;

    /**
     * 是否是菜单 0-菜单 1-目录 2-按钮
     */
    @Column(name = "is_menu")
    @ApiModelProperty(name = "isMenu",value = "是否是菜单 0-菜单 1-目录 2-按钮")
    private Integer isMenu;

    /**
     * 菜单地址
     */
    @ApiModelProperty(name = "url",value = "菜单地址")
    private String url;

    /**
     * 菜单图标
     */
    @ApiModelProperty(name = "icon",value = "菜单图标")
    private String icon;

    /**
     * 初始菜单id
     */
    @Column(name = "premenu_id")
    @ApiModelProperty(name = "premenuId",value = "初始菜单id")
    private String premenuId;

    /**
     * 菜单所属平台类型（1、WEB 2、PDA 3、平板）
     */
    @Column(name = "menu_type")
    @ApiModelProperty(name = "menuType",value = "菜单所属平台类型（1、WEB 2、Windows 3、PDA）")
    private Integer menuType;

    /**
     * 是否隐藏（0、否 1、是）
     */
    @Column(name = "is_hide")
    @ApiModelProperty(name = "isHide",value = "是否隐藏（0、否 1、是）")
    private Integer isHide;

    /**
     * 逻辑删除（0 正常,1 删除 ）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除（0 正常,1 删除 ）")
    private Long isDelete;

    /**
     * 打开方式（1、原始页 2、新页面）
     */
    @Column(name = "is_oppen")
    @ApiModelProperty(name = "isOppen",value = "打开方式（1、原始页 2、新页面）")
    private Long isOppen;


    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建用户Id")
    private String createUserId;

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
    @ApiModelProperty(name = "modifiedUserId",value = "修改用户id")
    private String modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

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