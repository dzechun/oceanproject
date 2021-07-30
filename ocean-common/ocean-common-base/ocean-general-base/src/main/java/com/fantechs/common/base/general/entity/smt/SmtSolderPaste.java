package com.fantechs.common.base.general.entity.smt;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasterConfig;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 锡膏管理
 * smt_solder_paste
 * @author mr.lei
 * @date 2021-07-22 10:57:06
 */
@Data
@Table(name = "smt_solder_paste")
public class SmtSolderPaste extends ValidGroup implements Serializable {
    /**
     * 锡膏管理ID
     */
    @ApiModelProperty(name="solderPasteId",value = "锡膏管理ID")
    @Excel(name = "锡膏管理ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "solder_paste_id")
    private Long solderPasteId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 锡膏状态(1-入冰库 2-回温 3-搅拌 4-开封 5-上料 6-用完 7-回冰 8-报废)
     */
    @ApiModelProperty(name="solderPasteStatus",value = "锡膏状态(1-入冰库 2-回温 3-搅拌 4-开封 5-上料 6-用完 7-回冰 8-报废)")
    @Transient
    private Byte solderPasteStatus;

    /**
     * 锡膏状态更新时间
     */
    @ApiModelProperty(name="spStatusUpdateTime",value = "锡膏状态更新时间")
    @Transient
    private Date spStatusUpdateTime;

    /**
     * 回温时间(分)
     */
    @ApiModelProperty(name="tBackTime",value = "回温时间(分)")
    @Excel(name = "回温时间(分)", height = 20, width = 30,orderNum="")
    @Column(name = "t_back_time")
    private BigDecimal tBackTime;

    /**
     * 搅拌时间(分)
     */
    @ApiModelProperty(name="stirTime",value = "搅拌时间(分)")
    @Excel(name = "搅拌时间(分)", height = 20, width = 30,orderNum="")
    @Column(name = "stir_time")
    private BigDecimal stirTime;

    /**
     * 已开封期限(小时)
     */
    @ApiModelProperty(name="openTimeLimit",value = "已开封期限(小时)")
    @Excel(name = "已开封期限(小时)", height = 20, width = 30,orderNum="") 
    @Column(name = "open_time_limit")
    private BigDecimal openTimeLimit;

    /**
     * 未开封期限(小时)
     */
    @ApiModelProperty(name="notOpenTimeLimit",value = "未开封期限(小时)")
    @Excel(name = "未开封期限(小时)", height = 20, width = 30,orderNum="") 
    @Column(name = "not_open_time_limit")
    private BigDecimal notOpenTimeLimit;

    /**
     * 回冰次数
     */
    @ApiModelProperty(name="returnIceTime",value = "回冰次数")
    @Excel(name = "回冰次数", height = 20, width = 30,orderNum="")
    @Column(name = "return_ice_time")
    private Integer returnIceTime;

    /**
     * 当前回冰次数
     */
    @ApiModelProperty(name="currentReturnIceTime",value = "当前回冰次数")
    @Transient
    private Integer currentReturnIceTime;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expirationDate",value = "过期日期")
    @Excel(name = "过期日期", height = 20, width = 30,orderNum="",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expiration_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    @Transient
    @ApiModelProperty(name = "smtSolderPasterConfig",value = "执行状态")
    private SmtSolderPasterConfig smtSolderPasterConfig;

    @Transient
    @ApiModelProperty(name = "smtSolderPasteJob",value = "更新记录（PDA显示）")
    private SmtSolderPasteJob smtSolderPasteJob;

    @Transient
    @ApiModelProperty(name = "message",value ="返回的信息")
    private String message;

    @Transient
    @ApiModelProperty(name = "executeStatus",value = "执行状态（0-通过 1-强制停止 2-警告）")
    private Integer executeStatus;

    @Transient
    @ApiModelProperty(name = "isDate",value = "是否过期日期（0-否 1-是）")
    private Integer isDate;

    private static final long serialVersionUID = 1L;
}