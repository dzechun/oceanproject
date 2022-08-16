package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 车间管理-返工单据
 * mes_sfc_rework_order
 * @author bgkun
 * @date 2021-06-15 11:08:53
 */
@Data
@Table(name = "mes_sfc_rework_order")
public class MesSfcReworkOrder extends ValidGroup implements Serializable {
    /**
     * 返工单ID
     */
    @ApiModelProperty(name="reworkOrderId",value = "返工单ID")
    @Excel(name = "返工单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "rework_order_id")
    private Long reworkOrderId;

    /**
     * 返工单号
     */
    @ApiModelProperty(name="reworkOrderCode",value = "返工单号")
    @Excel(name = "返工单号", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_order_code")
    private String reworkOrderCode;

    /**
     * 返工物料ID
     */
    @ApiModelProperty(name="materialId",value = "返工物料ID")
    @Excel(name = "返工物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 返工工艺路线ID
     */
    @ApiModelProperty(name="reworkRouteId",value = "返工工艺路线ID")
    @Excel(name = "返工工艺路线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_route_id")
    private Long reworkRouteId;

    /**
     * 返工开始工序ID
     */
    @ApiModelProperty(name="reworkStartProcessId",value = "返工开始工序ID")
    @Excel(name = "返工开始工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_start_process_id")
    private Long reworkStartProcessId;

    /**
     * 返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)
     */
    @ApiModelProperty(name="reworkStatus",value = "返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)")
    @Excel(name = "返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_status")
    private Byte reworkStatus;

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

    private static final long serialVersionUID = 1L;
}