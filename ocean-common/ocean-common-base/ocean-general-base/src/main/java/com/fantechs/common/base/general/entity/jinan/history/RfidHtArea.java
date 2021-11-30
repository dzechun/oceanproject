package com.fantechs.common.base.general.entity.jinan.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * RFID区域履历表
 * rfid_ht_area
 * @author admin
 * @date 2021-11-29 10:35:50
 */
@Data
@Table(name = "rfid_ht_area")
public class RfidHtArea extends ValidGroup implements Serializable {
    /**
     * 区域履历ID
     */
    @ApiModelProperty(name="htAreaId",value = "区域履历ID")
    @Excel(name = "区域履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_area_id")
    private Long htAreaId;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="areaId",value = "区域ID")
    @Excel(name = "区域ID", height = 20, width = 30,orderNum="") 
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode",value = "区域编码")
    @Excel(name = "区域编码", height = 20, width = 30,orderNum="") 
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(name="areaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30,orderNum="") 
    @Column(name = "area_name")
    private String areaName;

    /**
     * 区域描述
     */
    @ApiModelProperty(name="areaDesc",value = "区域描述")
    @Excel(name = "区域描述", height = 20, width = 30,orderNum="") 
    @Column(name = "area_desc")
    private String areaDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}