package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 关键物料清单历史表
 * base_ht_key_material
 * @author 53203
 * @date 2020-11-24 16:07:25
 */
@Data
@Table(name = "base_ht_key_material")
public class BaseHtKeyMaterial implements Serializable {
    /**
     * 关键物料历史id
     */
    @ApiModelProperty(name="htKeyMaterialId",value = "关键物料历史id")
    @Excel(name = "关键物料历史id", height = 20, width = 30)
    @Id
    @Column(name = "ht_key_material_id")
    private Long htKeyMaterialId;

    /**
     * 关键物料ID
     */
    @ApiModelProperty(name="keyMaterialId",value = "关键物料ID")
    @Excel(name = "关键物料ID", height = 20, width = 30)
    @Column(name = "key_material_id")
    private Long keyMaterialId;

    /**
     * 产品类别(2-产品型号 3-产品料号)
     */
    @ApiModelProperty(name="productType",value = "产品类别(2-产品型号 3-产品料号)")
    @Excel(name = "产品类别(2-产品型号 3-产品料号)", height = 20, width = 30)
    @Column(name = "product_type")
    private Byte productType;

    /**
     *  产品型号ID
     */
    @ApiModelProperty(name="productModelId",value = " 产品型号ID")
    @Excel(name = " 产品型号ID", height = 20, width = 30)
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     *  物料ID
     */
    @ApiModelProperty(name="materialId",value = " 物料ID")
    @Excel(name = " 物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 零件料号ID
     */
    @ApiModelProperty(name="partMaterialId",value = "零件料号ID")
    @Excel(name = "零件料号ID", height = 20, width = 30)
    @Column(name = "part_material_id")
    private Long partMaterialId;

    /**
     * 扫描枪数量
     */
    @ApiModelProperty(name="scanningGunQuantity",value = "扫描枪数量")
    @Excel(name = "扫描枪数量", height = 20, width = 30)
    @Column(name = "scanning_gun_quantity")
    private Integer scanningGunQuantity;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30)
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工位ID
     */
    @ApiModelProperty(name="stationId",value = "工位ID")
    @Excel(name = "工位ID", height = 20, width = 30)
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 用量
     */
    @ApiModelProperty(name="consumption",value = "用量")
    @Excel(name = "用量", height = 20, width = 30)
    private BigDecimal consumption;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

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
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

    /**
     *  产品型号编码
     */
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30)
    @Transient
    private String productModelCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @Transient
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    @Transient
    private String materialDesc;

    /**
     * 版本
     */
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "版本", height = 20, width = 30)
    @Transient
    private String version;

    /**
     * 零件料号
     */
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    @Excel(name = "零件料号", height = 20, width = 30)
    @Transient
    private String partMaterialCode;

    /**
     * 零件描述
     */
    @ApiModelProperty(name="partMaterialDesc" ,value="零件描述")
    @Excel(name = "零件描述", height = 20, width = 30)
    @Transient
    private String partMaterialDesc;

    /**
     * 零件料号版本
     */
    @ApiModelProperty(name="partMaterialVersion" ,value="零件料号版本")
    @Excel(name = "零件料号版本", height = 20, width = 30)
    @Transient
    private String partMaterialVersion;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    @Transient
    private String processName;

    /**
     * 工位名称
     */
    @ApiModelProperty(name = "stationName",value = "工位名称")
    @Excel(name = "工位名称", height = 20, width = 30)
    @Transient
    private String stationName;

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

    private static final long serialVersionUID = 1L;
}
