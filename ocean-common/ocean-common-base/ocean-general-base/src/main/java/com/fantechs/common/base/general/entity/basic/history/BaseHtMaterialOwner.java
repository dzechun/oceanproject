package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 货主信息履历表
 * base_ht_material_owner
 * @author admin
 * @date 2021-04-23 10:04:51
 */
@Data
@Table(name = "base_ht_material_owner")
public class BaseHtMaterialOwner extends ValidGroup implements Serializable {
    /**
     * 货主信息履历ID
     */
    @ApiModelProperty(name="htMaterialOwnerId",value = "货主信息履历ID")
    @Excel(name = "货主信息履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_material_owner_id")
    private Long htMaterialOwnerId;

    /**
     * 货主信息ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息ID")
    @Excel(name = "货主信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 货主编码
     */
    @ApiModelProperty(name="materialOwnerCode",value = "货主编码")
    @Excel(name = "货主编码", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_code")
    private String materialOwnerCode;

    /**
     * 货主名称
     */
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    @Excel(name = "货主名称", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_name")
    private String materialOwnerName;

    /**
     * 货主简称
     */
    @ApiModelProperty(name="materialOwnerShortName",value = "货主简称")
    @Excel(name = "货主简称", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_short_name")
    private String materialOwnerShortName;

    /**
     * 联系人
     */
    @ApiModelProperty(name="linkManName",value = "联系人")
    @Excel(name = "联系人", height = 20, width = 30,orderNum="") 
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系电话")
    @Excel(name = "联系电话", height = 20, width = 30,orderNum="") 
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真
     */
    @ApiModelProperty(name="faxNumber",value = "传真")
    @Excel(name = "传真", height = 20, width = 30,orderNum="") 
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(name="emailAddress",value = "邮箱")
    @Excel(name = "邮箱", height = 20, width = 30,orderNum="") 
    @Column(name = "e_mail_address")
    private String emailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="address",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="") 
    private String address;

    /**
     * 描述
     */
    @ApiModelProperty(name="materialOwnerDesc",value = "描述")
    @Excel(name = "描述", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_desc")
    private String materialOwnerDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

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
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}