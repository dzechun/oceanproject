package com.fantechs.common.base.entity.storage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 栈板/包箱-储位关系表（smt）
 * smt_storage_pallet
 * @author hyc
 * @date 2021-01-07 16:16:16
 */
@Data
@Table(name = "smt_storage_pallet")
public class SmtStoragePallet extends ValidGroup implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty(name="storagePalletId",value = "ID")
    @Id
    @Column(name = "storage_pallet_id")
    @NotNull(groups = update.class,message = "ID不能为空")
    private Long storagePalletId;

    /**
     * 栈板号/箱号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30)
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30)
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 栈板类型（0、栈板 1、包箱）
     */
    @ApiModelProperty(name="palletType",value = "栈板类型（0、栈板 1、包箱）")
    @Excel(name = "栈板类型（0、栈板 1、包箱）", height = 20, width = 30)
    @Column(name = "pallet_type")
    private Byte palletType;

    /**
     * 是否绑定（0、否 1、是）
     */
    @ApiModelProperty(name="isBinding",value = "是否绑定（0、否 1、是）")
    @Excel(name = "是否绑定（0、否 1、是）", height = 20, width = 30)
    @Column(name = "is_binding")
    private Byte isBinding;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30)
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
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
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
    @Excel(name = "修改人ID", height = 20, width = 30)
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

    private static final long serialVersionUID = 1L;
}