package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 预警信息表
 * base_warning
 * @author 53203
 * @date 2021-03-03 15:08:33
 */
@Data
@Table(name = "base_warning")
public class BaseWarning extends ValidGroup implements Serializable {

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    @Id
    @Column(name = "warning_id")
    @NotNull(groups = update.class,message = "预警ID不能为空")
    private Long warningId;

    /**
     * 预警类型
     */
    @ApiModelProperty(name="warningType",value = "预警类型")
    @Excel(name = "预警类型", height = 20, width = 30,replace = {"质检预警_0","生产线_1","缺料预警_2","安全库存预警_3"})
    @Column(name = "warning_type")
    @NotNull(message = "预警类型不能为空")
    private Long warningType;

    /**
     * 人员等级（0、一级人员 1、二级人员 2、三级人员）
     */
    @ApiModelProperty(name="personnelLevel",value = "人员等级（0、一级人员 1、二级人员 2、三级人员）")
    @Excel(name = "人员等级（0、一级人员 1、二级人员 2、三级人员）", height = 20, width = 30,replace = {"一级人员_0","二级人员_1","三级人员_2","四级人员_3"})
    @Column(name = "personnel_level")
    @NotNull(message = "人员等级不能为空")
    private Byte personnelLevel;

    /**
     * 通知方式（0、微信 1、短信 2、钉钉 3、邮件）
     */
    @ApiModelProperty(name="notificationMethod",value = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）")
    @Excel(name = "通知方式（0、微信 1、短信 2、钉钉 3、邮件）", height = 20, width = 30,replace = {"微信_0","短信_1","钉钉_2","邮件_3"})
    @Column(name = "notification_method")
    @NotNull(message = "通知方式不能为空")
    private Byte notificationMethod;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,replace = {"无效_0","有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

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
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 预警人员关系集合
     */
    @ApiModelProperty(name="baseWarningPersonnelList",value = "预警人员关系集合")
    @Transient
    private List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}