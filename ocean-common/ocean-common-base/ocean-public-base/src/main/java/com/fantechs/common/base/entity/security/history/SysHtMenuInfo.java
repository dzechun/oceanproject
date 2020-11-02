package com.fantechs.common.base.entity.security.history;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_ht_menuinfo")
@Data
public class SysHtMenuInfo {
    /**
     * 菜单履历id
     */
    @Id
    @Column(name = "ht_menu_id")
    private Long htMenuId;

    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    @ApiModelProperty(name = "menuId",value = "菜单id")
    private Long menuId;

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
    private Integer orderNum;

    /**
     * 父级菜单id
     */
    @Column(name = "parent_id")
    @ApiModelProperty(name = "parentId",value = "父级菜单id")
    private Long parentId;

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
     * 初始菜单id
     */
    @Column(name = "premenu_id")
    @ApiModelProperty(name = "premenuId",value = "初始菜单id")
    private String premenuId;

    /**
     * 菜单所属平台类型（1、WEB 2、Windows 3、PDA）
     */
    @Column(name = "menu_type")
    @ApiModelProperty(name = "menuType",value = "菜单所属平台类型（1、WEB 2、Windows 3、PDA）")
    private Byte menuType;

    /**
     * 是否隐藏（0、否 1、是）
     */
    @Column(name = "is_hide")
    @ApiModelProperty(name = "isHide",value = "是否隐藏（0、否 1、是）")
    private Byte isHide;

    /**
     * 逻辑删除（0 删除,1 正常 ）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除（0 删除,1 正常 ）")
    private Byte isDelete;

    /**
     * 菜单图标
     */
    @ApiModelProperty(name = "icon",value = "菜单图标")
    private String icon;

    /**
     * 打开方式（1、原始页 2、新页面）
     */
    @Column(name = "is_oppen")
    @ApiModelProperty(name = "isOppen",value = "打开方式（1、原始页 2、新页面）")
    private Byte isOppen;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人id
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
}