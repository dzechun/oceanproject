package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 其他出库
 * wms_out_ht_otherout
 * @author hyc
 * @date 2020-12-31 09:54:52
 */
@Data
@Table(name = "wms_out_ht_otherout")
public class WmsOutHtOtherout implements Serializable {
    @Id
    @Column(name = "ht_otherout_id")
    private Long htOtheroutId;

    /**
     * 其他出库单ID
     */
    @ApiModelProperty(name="otheroutId",value = "其他出库单ID")
    @Excel(name = "其他出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_id")
    private Long otheroutId;

    /**
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    @Excel(name = "其他出库单号", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_code")
    private String otheroutCode;

    /**
     * 出库类型（1、杂出 2、未知）
     */
    @ApiModelProperty(name="otheroutType",value = "出库类型（1、杂出 2、未知）")
    @Excel(name = "出库类型（1、杂出 2、未知）", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_type")
    private Byte otheroutType;

    /**
     * 出库时间
     */
    @ApiModelProperty(name="otheroutTime",value = "出库时间")
    @Excel(name = "出库时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "otherout_time")
    private Date otheroutTime;

    /**
     * 操作人id
     */
    @ApiModelProperty(name="operatorId",value = "操作人id")
    @Excel(name = "操作人id", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 单据状态（0-待出库 1-出货中 2-出货完成）
     */
    @ApiModelProperty(name="otheroutStatus",value = "单据状态（0-待出库 1-出货中 2-出货完成）")
    @Excel(name = "单据状态（0-待出库 1-出货中 2-出货完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "otherout_status")
    private Byte otheroutStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Column(name = "organization_name")
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 操作人名称
     */
    @Transient
    @ApiModelProperty(name = "operatorName",value = "操作人名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="4")
    private String operatorName;

    private static final long serialVersionUID = 1L;
}