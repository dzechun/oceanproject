package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 设备绑定产品清单履历表
 * eam_ht_equipment_material_list
 * @author admin
 * @date 2021-06-28 14:09:03
 */
@Data
@Table(name = "eam_ht_equipment_material_list")
public class EamHtEquipmentMaterialList extends ValidGroup implements Serializable {
    /**
     * 设备绑定产品清单履历ID
     */
    @ApiModelProperty(name="htEquipmentMaterialListId",value = "设备绑定产品清单履历ID")
    @Excel(name = "设备绑定产品清单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_material_list_id")
    private Long htEquipmentMaterialListId;

    /**
     * 设备绑定产品清单ID
     */
    @ApiModelProperty(name="equipmentMaterialListId",value = "设备绑定产品清单ID")
    @Excel(name = "设备绑定产品清单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_material_list_id")
    private Long equipmentMaterialListId;

    /**
     * 设备绑定产品表头ID
     */
    @ApiModelProperty(name="equipmentMaterialId",value = "设备绑定产品表头ID")
    @Excel(name = "设备绑定产品表头ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_material_id")
    private Long equipmentMaterialId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="1")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="2")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion",value = "物料版本")
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialVersion;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}