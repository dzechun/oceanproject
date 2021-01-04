package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 其他出库
 * wms_out_otherout
 * @author 53203
 * @date 2020-12-25 16:40:34
 */
@Data
@Table(name = "wms_out_otherout")
public class WmsOutOtherout extends ValidGroup implements Serializable {
    /**
     * 其他出库单ID
     */
    @ApiModelProperty(name="otheroutId",value = "其他出库单ID")
    @Id
    @Column(name = "otherout_id")
    @NotNull(groups = update.class,message = "出库单ID不能为空")
    private Long otheroutId;

    /**
     * 其他出库单号
     */
    @ApiModelProperty(name="otheroutCode",value = "其他出库单号")
    @Excel(name = "其他出库单号", height = 20, width = 30,orderNum="1")
    @Column(name = "otherout_code")
    private String otheroutCode;

    /**
     * 出库类型（1、杂出 2、未知）
     */
    @ApiModelProperty(name="otheroutType",value = "出库类型（1、杂出 2、未知）")
    @Excel(name = "出库类型（1、杂出 2、未知）", height = 20, width = 30,orderNum="2",replace = {"杂出_1","未知_2"})
    @Column(name = "otherout_type")
    private Byte otheroutType;

    /**
     * 出库时间
     */
    @ApiModelProperty(name="otheroutTime",value = "出库时间")
    @Excel(name = "出库时间", height = 20, width = 30,orderNum="3",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "otherout_time")
    @NotNull(message = "出库时间不能为空")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date otheroutTime;

    /**
     * 操作人Id
     */
    @ApiModelProperty(name="operator_id",value = "操作人Id")
    @Column(name = "operator_id")
    @NotNull(message = "操作人id不能为空")
    private Long operatorId;

    /**
     * 单据状态（0-待出库 1-出货中 2-出货完成）
     */
    @ApiModelProperty(name="otheroutStatus",value = "单据状态（0-待出库 1-出货中 2-出货完成）")
    @Excel(name = "单据状态（0-待出库 1-出货中 2-出货完成）", height = 20, width = 30,orderNum="5")
    @Column(name = "otherout_status")
    private Byte otheroutStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="6")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 其他出库明细
     */
    @ApiModelProperty(name="wmsOutOtheroutDets",value = "其他出库明细）")
    private List<WmsOutOtheroutDet> wmsOutOtheroutDets;

    private static final long serialVersionUID = 1L;
}