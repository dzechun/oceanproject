package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.basic.SmtProductBomDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "smt_product_bom")
@Data
public class SmtProductBom extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 8177452637204613229L;

    /**
     * 产品BOM ID
     */
    @Id
    @Column(name = "product_bom_id")
    @ApiModelProperty(name = "productBomId", value = "产品BOM ID")
    @NotNull(groups = update.class, message = "产品BOM ID不能为空")
    private Long productBomId;

    /**
     * 产品BOM编号
     */
    @Column(name = "product_bom_code")
    @ApiModelProperty(name = "productBomCode", value = "BOM ID")
    @Excel(name = "BOM ID", height = 20, width = 30)
    @NotBlank(message = "产品BOM编号不能为空")
    private String productBomCode;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name = "materialId", value = "产品料号ID")
    @NotNull(message = "物料id不能为空")
    private Long materialId;

    /**
     * 线别ID
     */
    @Column(name = "pro_line_id")
    @ApiModelProperty(name = "proLineId", value = "线别ID")
    private Long proLineId;

    /**
     * 代用物料ID
     */
    @Column(name = "sub_material_id")
    @ApiModelProperty(name="subMaterialId" ,value="代用物料ID")
    private Long subMaterialId;

    /**
     * 用量
     */
    @ApiModelProperty(name="quantity" ,value="用量")
    @Excel(name = "用量", height = 20, width = 30)
    @Column(name = "quantity")
    private BigDecimal quantity;

    /**
     * 基准数量
     */
    @ApiModelProperty(name="baseQuantity" ,value="基准数量")
    @Excel(name = "基准数量", height = 20, width = 30)
    @Column(name = "base_quantity")
    private BigDecimal baseQuantity;

    /**
     * 位置
     */
    @ApiModelProperty(name="position" ,value="位置")
    @Excel(name = "位置", height = 20, width = 30)
    private String position;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 配送方式（0、配送优化 1、配送不优化  2、不配送不优化）
     */
    @Column(name = "delivery_mode")
    @ApiModelProperty(name = "deliveryMode", value = "配送方式（0、配送优化 1、配送不优化  2、不配送不优化）")
    private Integer deliveryMode;

    /**
     * 发料方式（0、推式  1、拉式）
     */
    @Column(name = "issue_method")
    @ApiModelProperty(name = "issueMethod", value = "发料方式（0、推式  1、拉式）")
    private Integer issueMethod;

    /**
     * 损耗率
     */
    @Column(name = "loss_rate")
    @ApiModelProperty(name = "lossRate", value = "损耗率")
    private BigDecimal lossRate;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name = "takt", value = "节拍数量(秒)")
    private Integer takt;

    /**
     * 移转数量
     */
    @Column(name = "transfer_quantity")
    @ApiModelProperty(name = "transferQuantity", value = "移转数量")
    private Integer transferQuantity;

    /**
     * BOM状态(1-未核准 2-已核准)
     */
    @Column(name = "bom_status")
    @ApiModelProperty(name = "bomStatus", value = "BOM状态(1-未核准 2-已核准)")
    private Byte bomStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name = "organizationId", value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name = "status", value = "状态（0、无效 1、有效）")
    @Excel(name = "状态", height = 20, width = 30, replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 父BOM ID
     */
    @Column(name = "parent_bom_id")
    @ApiModelProperty(name = "parentBomId", value = "父BOM ID")
    private Long parentBomId;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId", value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId", value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime", value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete", value = "逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

    /*    @ApiModelProperty(name = "smtProductBomDets",value = "产品BOM明细")
        @Transient
        private List<SmtProductBomDet> smtProductBomDets;*/

    @ApiModelProperty(name = "smtProductBoms", value = "产品BOM集合")
    @Transient
    private List<SmtProductBomDto> smtProductBomDtos;
}
