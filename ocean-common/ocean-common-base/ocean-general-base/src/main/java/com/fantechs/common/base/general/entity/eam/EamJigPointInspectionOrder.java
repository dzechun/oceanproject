package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionOrderDetDto;
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
 * 治具点检单
 * eam_jig_point_inspection_order
 * @author admin
 * @date 2021-08-16 11:36:35
 */
@Data
@Table(name = "eam_jig_point_inspection_order")
public class EamJigPointInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 治具点检单ID
     */
    @ApiModelProperty(name="jigPointInspectionOrderId",value = "治具点检单ID")
    @Id
    @Column(name = "jig_point_inspection_order_id")
    @NotNull(groups = update.class,message = "治具点检单ID不能为空")
    private Long jigPointInspectionOrderId;

    /**
     * 治具点检单号
     */
    @ApiModelProperty(name="jigPointInspectionOrderCode",value = "治具点检单号")
    @Excel(name = "治具点检单号", height = 20, width = 30,orderNum="1")
    @Column(name = "jig_point_inspection_order_code")
    private String jigPointInspectionOrderCode;

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
     * 治具点检项目ID
     */
    @ApiModelProperty(name="jigPointInspectionProjectId",value = "治具点检项目ID")
    @Column(name = "jig_point_inspection_project_id")
    private Long jigPointInspectionProjectId;

    /**
     * 单据状态(1-待点检 2-已点检)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待点检 2-已点检)")
    @Excel(name = "单据状态(1-待点检 2-已点检)", height = 20, width = 30,orderNum="8")
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
     * 治具点检单事项列表
     */
    @ApiModelProperty(name="list",value = "治具点检单事项列表")
    private List<EamJigPointInspectionOrderDetDto> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}