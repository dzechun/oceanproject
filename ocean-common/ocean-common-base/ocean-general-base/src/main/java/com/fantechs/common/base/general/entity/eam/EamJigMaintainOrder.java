package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 治具保养单
 * eam_jig_maintain_order
 * @author admin
 * @date 2021-08-13 09:16:44
 */
@Data
@Table(name = "eam_jig_maintain_order")
public class EamJigMaintainOrder extends ValidGroup implements Serializable {
    /**
     * 治具保养单ID
     */
    @ApiModelProperty(name="jigMaintainOrderId",value = "治具保养单ID")
    @Id
    @Column(name = "jig_maintain_order_id")
    @NotNull(groups = update.class,message = "治具保养单ID不能为空")
    private Long jigMaintainOrderId;

    /**
     * 治具保养单号
     */
    @ApiModelProperty(name="jigMaintainOrderCode",value = "治具保养单号")
    @Excel(name = "治具保养单号", height = 20, width = 30,orderNum="1")
    @Column(name = "jig_maintain_order_code")
    private String jigMaintainOrderCode;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    @Column(name = "jig_barcode_id")
    @NotNull(message = "治具条码不能为空")
    private Long jigBarcodeId;

    /**
     * 治具保养项目ID
     */
    @ApiModelProperty(name="jigMaintainProjectId",value = "治具保养项目ID")
    @Column(name = "jig_maintain_project_id")
    private Long jigMaintainProjectId;

    /**
     * 单据状态(1-待保养 2-已保养)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待保养 2-已保养)")
    @Excel(name = "单据状态(1-待保养 2-已保养)", height = 20, width = 30,orderNum="8")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 保养单明细
     */
    @ApiModelProperty(name="list",value = "保养单明细")
    private List<EamJigMaintainOrderDetDto> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}