package com.fantechs.common.base.general.entity.daq;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 设备工作站关系履历表
 * daq_ht_equipment_re_es
 * @author 81947
 * @date 2021-08-09 09:23:15
 */
@Data
@Table(name = "daq_ht_equipment_re_es")
public class DaqHtEquipmentReEs extends ValidGroup implements Serializable {
    /**
     * 设备设备工作站关系履历表ID
     */
    @ApiModelProperty(name="htEquipmentReEsId",value = "设备设备工作站关系履历表ID")
    @Excel(name = "设备设备工作站关系履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_re_es_id")
    private Long htEquipmentReEsId;

    /**
     * 设备设备工作站关系表ID
     */
    @ApiModelProperty(name="equipmentReEsId",value = "设备设备工作站关系表ID")
    @Excel(name = "设备设备工作站关系表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_re_es_id")
    private Long equipmentReEsId;

    /**
     * 设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "设备ID")
    @Excel(name = "设备ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备工作站ID
     */
    @ApiModelProperty(name="equipmentStationId",value = "设备工作站ID")
    @Excel(name = "设备工作站ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_station_id")
    private Long equipmentStationId;

    /**
     * 横坐标
     */
    @ApiModelProperty(name="locationX",value = "横坐标")
    @Excel(name = "横坐标", height = 20, width = 30,orderNum="") 
    @Column(name = "location_x")
    private BigDecimal locationX;

    /**
     * 纵坐标
     */
    @ApiModelProperty(name="locationY",value = "纵坐标")
    @Excel(name = "纵坐标", height = 20, width = 30,orderNum="") 
    @Column(name = "location_y")
    private BigDecimal locationY;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}