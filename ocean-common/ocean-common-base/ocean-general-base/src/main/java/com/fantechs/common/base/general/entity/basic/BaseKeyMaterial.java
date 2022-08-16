package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 关键物料清单
 * base_key_material
 * @author 53203
 * @date 2020-11-24 16:07:24
 */
@Data
@Table(name = "base_key_material")
public class BaseKeyMaterial extends ValidGroup implements Serializable {
    /**
     * 关键物料ID
     */
    @ApiModelProperty(name="keyMaterialId",value = "关键物料ID")
    @Id
    @Column(name = "key_material_id")
    @NotNull(groups = update.class,message = "关键物料id不能为空")
    private Long keyMaterialId;

    /**
     * 产品类别(2-产品型号 3-产品料号)
     */
    @ApiModelProperty(name="productType",value = "产品类别(2-产品型号 3-产品料号)")
    @Excel(name = "产品类别(2-产品型号 3-产品料号)", height = 20, width = 30,orderNum="1",replace = {"产品型号_2","产品料号_3"})
    @Column(name = "product_type")
    @NotNull(message = "产品类别不能为空")
    private Byte productType;

    /**
     *  产品型号ID
     */
    @ApiModelProperty(name="productModelId",value = " 产品型号ID")
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     *  产品料号id
     */
    @ApiModelProperty(name="materialId",value = " 产品料号id")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 零件料号ID
     */
    @ApiModelProperty(name="partMaterialId",value = "零件料号ID")
    @Column(name = "part_material_id")
    @NotNull(message = "零件料号不能为空")
    private Long partMaterialId;

    /**
     * 扫描枪数量
     */
    @ApiModelProperty(name="scanningGunQuantity",value = "扫描枪数量")
    @Excel(name = "扫描枪数量", height = 20, width = 30,orderNum="9")
    @Column(name = "scanning_gun_quantity")
    private Integer scanningGunQuantity;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工位ID
     */
    @ApiModelProperty(name="stationId",value = "工位ID")
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 用量
     */
    @ApiModelProperty(name="consumption",value = "用量")
    @Excel(name = "用量", height = 20, width = 30,orderNum="12")
    private BigDecimal consumption;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="13",replace = {"无效_0","有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
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

    private static final long serialVersionUID = 1L;
}
