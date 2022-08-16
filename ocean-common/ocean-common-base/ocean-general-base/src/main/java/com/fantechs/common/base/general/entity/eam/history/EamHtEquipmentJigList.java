package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 设备绑定治具清单履历表
 * eam_ht_equipment_jig_list
 * @author Dylan
 * @date 2021-08-20 14:28:58
 */
@Data
@Table(name = "eam_ht_equipment_jig_list")
public class EamHtEquipmentJigList extends ValidGroup implements Serializable {
    /**
     * 设备绑定治具清单履历ID
     */
    @ApiModelProperty(name="htEquipmentJigListId",value = "设备绑定治具清单履历ID")
    @Excel(name = "设备绑定治具清单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_jig_list_id")
    private Long htEquipmentJigListId;

    /**
     * 设备绑定治具清单ID
     */
    @ApiModelProperty(name="equipmentJigListId",value = "设备绑定治具清单ID")
    @Excel(name = "设备绑定治具清单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_jig_list_id")
    private Long equipmentJigListId;

    /**
     * 设备绑定治具表头ID
     */
    @ApiModelProperty(name="equipmentJigId",value = "设备绑定治具表头ID")
    @Excel(name = "设备绑定治具表头ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_jig_id")
    private Long equipmentJigId;

    /**
     * 治具ID
     */
    @ApiModelProperty(name="jigId",value = "治具ID")
    @Excel(name = "治具ID", height = 20, width = 30,orderNum="") 
    @Column(name = "jig_id")
    private Long jigId;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="usageQty",value = "使用数量")
    @Excel(name = "使用数量", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_qty")
    private Integer usageQty;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="1")
    @Transient
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="2")
    @Transient
    private String jigName;

    /**
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="3")
    @Transient
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="4")
    @Transient
    private String jigModel;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}