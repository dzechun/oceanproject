package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 物料关联标签信息
 * bcm_label_material
 * @author mr.lei
 * @date 2020-12-17 19:34:45
 */
@Data
@Table(name = "base_label_material")
public class BaseLabelMaterial extends ValidGroup implements Serializable {
    /**
     * 物料关联标签id
     */
    @ApiModelProperty(name="labelMaterialId",value = "物料关联标签id")
    @Id
    @Column(name = "label_material_id")
    private Long labelMaterialId;

    /**
     * 产品物料id
     */
    @ApiModelProperty(name="materialId",value = "产品物料id")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 标签信息id
     */
    @ApiModelProperty(name="labelId",value = "标签信息id")
    @Column(name = "label_id")
    private Long labelId;

    /**
     * 工序信息id
     */
    @ApiModelProperty(name="processId",value = "工序信息id")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    /**
     * 是否工序打印（Y/N）
     */
    @ApiModelProperty(name="isProcess",value = "是否工序打印（Y/N）")
    @Column(name = "is_process")
    private Byte isProcess;

    /**
     * 一次打印数量
     */
    @ApiModelProperty(name = "oncePrintCount",value = "一次打印数量")
    @Column(name = "once_print_count")
    private Integer oncePrintCount;

    private static final long serialVersionUID = 1L;
}