package com.fantechs.common.base.general.entity.eam.history;

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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 台账管理履历表
 * eam_ht_standing_book
 * @author admin
 * @date 2021-06-25 15:33:12
 */
@Data
@Table(name = "eam_ht_standing_book")
public class EamHtStandingBook extends ValidGroup implements Serializable {
    /**
     * 台账管理履历ID
     */
    @ApiModelProperty(name="htStandingBookId",value = "台账管理履历ID")
    @Excel(name = "台账管理履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_standing_book_id")
    private Long htStandingBookId;

    /**
     * 台账管理ID
     */
    @ApiModelProperty(name="standingBookId",value = "台账管理ID")
    @Excel(name = "台账管理ID", height = 20, width = 30,orderNum="") 
    @Column(name = "standing_book_id")
    private Long standingBookId;

    /**
     * 设备信息ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备信息ID")
    @Excel(name = "设备信息ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="1")
    @Column(name = "asset_code")
    private String assetCode;

    /**
     * 重要程度(1-固定资产 2-列管品)
     */
    @ApiModelProperty(name="degreeOfImportance",value = "重要程度(1-固定资产 2-列管品)")
    @Excel(name = "重要程度(1-固定资产 2-列管品)", height = 20, width = 30,orderNum="") 
    @Column(name = "degree_of_importance")
    private Byte degreeOfImportance;

    /**
     * 归属部门ID
     */
    @ApiModelProperty(name="deptId",value = "归属部门ID")
    @Excel(name = "归属部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 折旧年限
     */
    @ApiModelProperty(name="depreciableLife",value = "折旧年限")
    @Excel(name = "折旧年限", height = 20, width = 30,orderNum="") 
    @Column(name = "depreciable_life")
    private BigDecimal depreciableLife;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 设备编码
     */
    @Transient
    @ApiModelProperty(name = "equipmentCode",value = "设备编码")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 设备描述
     */
    @Transient
    @ApiModelProperty(name = "equipmentDesc",value = "设备描述")
    @Excel(name = "设备描述", height = 20, width = 30,orderNum="2")
    private String equipmentDesc;

    /**
     * 设备型号
     */
    @Transient
    @ApiModelProperty(name = "equipmentModel",value = "设备型号")
    @Excel(name = "设备型号", height = 20, width = 30,orderNum="3")
    private String equipmentModel;

    /**
     * 设备类别
     */
    @Transient
    @ApiModelProperty(name = "equipmentCategoryDesc",value = "设备类别")
    @Excel(name = "设备类别", height = 20, width = 30,orderNum="3")
    private String equipmentCategoryDesc;

    /**
     * 功率(kw)
     */
    @Transient
    @ApiModelProperty(name = "power",value = "功率(kw)")
    @Excel(name = "功率(kw)", height = 20, width = 30,orderNum="3")
    private BigDecimal power;

    /**
     * 重量(kg)
     */
    @Transient
    @ApiModelProperty(name = "weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30,orderNum="3")
    private BigDecimal weight;

    /**
     * 出厂日期
     */
    @Transient
    @ApiModelProperty(name = "releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="3")
    private Date releaseDate;

    /**
     * 部门名称
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门名称")
    private String deptName;

    /**
     * 使用状态(1-使用中 2-空闲)
     */
    @Transient
    @ApiModelProperty(name = "usageStatus",value = "使用状态(1-使用中 2-空闲)")
    @Excel(name = "使用状态(1-使用中 2-空闲)", height = 20, width = 30,orderNum="12")
    private Byte usageStatus;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}