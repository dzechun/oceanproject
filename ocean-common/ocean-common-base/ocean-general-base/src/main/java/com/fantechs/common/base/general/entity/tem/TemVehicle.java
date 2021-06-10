package com.fantechs.common.base.general.entity.tem;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 周转工具管理
 * tem_vehicle
 * @author 86178
 * @date 2021-06-08 18:37:33
 */
@Data
@Table(name = "tem_vehicle")
public class TemVehicle extends ValidGroup implements Serializable {
    /**
     * 周转工具ID
     */
    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    @Excel(name = "周转工具ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "vehicle_id")
    @NotNull(groups = update.class, message = "周转工具ID不能为空")
    private Long vehicleId;

    /**
     * 周转工具编码
     */
    @ApiModelProperty(name="vehicleCode",value = "周转工具编码")
    @Excel(name = "周转工具编码", height = 20, width = 30,orderNum="") 
    @Column(name = "vehicle_code")
    private String vehicleCode;

    /**
     * 周转工具名称
     */
    @ApiModelProperty(name="vehicleName",value = "周转工具名称")
    @Excel(name = "周转工具名称", height = 20, width = 30,orderNum="") 
    @Column(name = "vehicle_name")
    private String vehicleName;

    /**
     * 周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)
     */
    @ApiModelProperty(name="vehicleStatus",value = "周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)")
    @Excel(name = "周转工具状态(1-空闲 2-出库中 3-使用中 4-入库中)", height = 20, width = 30,orderNum="") 
    @Column(name = "vehicle_status")
    private Byte vehicleStatus;

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

    private static final long serialVersionUID = 1L;
}