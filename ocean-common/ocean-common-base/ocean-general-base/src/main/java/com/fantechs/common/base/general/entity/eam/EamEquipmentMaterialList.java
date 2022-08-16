package com.fantechs.common.base.general.entity.eam;

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
;

/**
 * 设备绑定产品清单
 * eam_equipment_material_list
 * @author admin
 * @date 2021-06-28 14:09:03
 */
@Data
@Table(name = "eam_equipment_material_list")
public class EamEquipmentMaterialList extends ValidGroup implements Serializable {
    /**
     * 设备绑定产品清单ID
     */
    @ApiModelProperty(name="equipmentMaterialListId",value = "设备绑定产品清单ID")
    @Id
    @Column(name = "equipment_material_list_id")
    @NotNull(groups = update.class,message = "设备绑定产品清单ID不能为空")
    private Long equipmentMaterialListId;

    /**
     * 设备绑定产品表头ID
     */
    @ApiModelProperty(name="equipmentMaterialId",value = "设备绑定产品表头ID")
    @Column(name = "equipment_material_id")
    private Long equipmentMaterialId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    @NotNull(message = "物料信息不能为空")
    private Long materialId;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="usageQty",value = "使用数量")
    @Column(name = "usage_qty")
    @NotNull(message = "使用数量不能为空")
    private Integer usageQty;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}