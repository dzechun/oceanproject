package com.fantechs.common.base.general.entity.basic;

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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

;
;

/**
 * 产品BOM子料
 * base_product_bom_det
 * @author 81947
 * @date 2021-06-09 20:37:07
 */
@Data
@Table(name = "base_product_bom_det")
public class BaseProductBomDet extends ValidGroup implements Serializable {
    /**
     * 产品BOM子料ID
     */
    @ApiModelProperty(name="productBomDetId",value = "产品BOM子料ID")
    @Excel(name = "产品BOM子料ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "product_bom_det_id")
    private Long productBomDetId;

    /**
     * 产品BOM父料ID
     */
    @ApiModelProperty(name="productBomId",value = "产品BOM父料ID")
    @Excel(name = "产品BOM父料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_bom_id")
    private Long productBomId;

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId",value = "产品料号ID")
    @Excel(name = "产品料号ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 配送方式 （1、配送优化 2、配送不优化  2、不配送不优化）
     */
    @ApiModelProperty(name="dispatchType",value = "配送方式 （1、配送优化 2、配送不优化  2、不配送不优化）")
    @Excel(name = "配送方式 （1、配送优化 2、配送不优化  2、不配送不优化）", height = 20, width = 30,orderNum="") 
    @Column(name = "dispatch_type")
    private Byte dispatchType;

    /**
     * 发料方式 （1、推式  2、拉式）
     */
    @ApiModelProperty(name="issueMethod",value = "发料方式 （1、推式  2、拉式）")
    @Excel(name = "发料方式 （1、推式  2、拉式）", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_method")
    private Byte issueMethod;

    /**
     * 损耗率
     */
    @ApiModelProperty(name="lossRate",value = "损耗率")
    @Excel(name = "损耗率", height = 20, width = 30,orderNum="") 
    @Column(name = "loss_rate")
    private Integer lossRate;

    /**
     * 移转数量
     */
    @ApiModelProperty(name="transferQty",value = "移转数量")
    @Excel(name = "移转数量", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_qty")
    private Integer transferQty;

    /**
     * 替代料ID
     */
    @ApiModelProperty(name="subMaterialId",value = "替代料ID")
    @Excel(name = "替代料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "sub_material_id")
    private Long subMaterialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 用量
     */
    @ApiModelProperty(name="usageQty",value = "用量")
    @Excel(name = "用量", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_qty")
    private BigDecimal usageQty;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQty",value = "基准数量")
    @Excel(name = "基准数量", height = 20, width = 30,orderNum="") 
    @Column(name = "base_qty")
    private BigDecimal baseQty;

    /**
     * 位置
     */
    @ApiModelProperty(name="position",value = "位置")
    @Excel(name = "位置", height = 20, width = 30,orderNum="") 
    private String position;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 是否有下一级（0、否 1、是）
     */
    @ApiModelProperty(name="ifHaveLowerLevel",value = "是否有下一级（0、否 1、是）")
    @Column(name = "if_have_lower_level")
    private Byte ifHaveLowerLevel;


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

    @ApiModelProperty(name = "baseProductBomDets", value = "产品BOM集合")
    @Transient
    private List<BaseProductBomDet> baseProductBomDets;

    /**
     * 父BomDetID
     */
    @ApiModelProperty(name="parentProductBomDetId",value = "父BomDetID")
    @Transient
    private Long parentProductBomDetId;
}