package com.fantechs.common.base.general.entity.om;

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
 * 销售编码关联同包装编码
 * om_sales_code_re_spc
 * @author bgkun
 * @date 2021-10-15 10:14:00
 */
@Data
@Table(name = "om_sales_code_re_spc")
public class OmSalesCodeReSpc extends ValidGroup implements Serializable {
    /**
     * 销售编码关联同包装表ID
     */
    @ApiModelProperty(name="salesCodeReSpcId",value = "销售编码关联同包装表ID")
    @Excel(name = "销售编码关联同包装表ID", height = 20, width = 30) 
    @Id
    @Column(name = "sales_code_re_spc_id")
    private Long salesCodeReSpcId;

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30) 
    @Column(name = "sales_code")
    private String salesCode;

    /**
     * 同包装编码
     */
    @ApiModelProperty(name="samePackageCode",value = "同包装编码")
    @Excel(name = "同包装编码", height = 20, width = 30) 
    @Column(name = "same_package_code")
    private String samePackageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialId",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30) 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 客户型号ID
     */
    @ApiModelProperty(name="productModelId",value = "客户型号ID")
    @Excel(name = "客户型号ID", height = 20, width = 30)
    @Column(name = "product_model_id")
    private Long productModelId;

    /**
     * 优先级
     */
    @ApiModelProperty(name="priority",value = "优先级")
    @Excel(name = "优先级", height = 20, width = 30) 
    private Integer priority;

    /**
     * 同包装编码状态(1:激活;2:失效;3：关闭；)
     */
    @ApiModelProperty(name="samePackageCodeStatus",value = "同包装编码状态(1:激活;2:失效;3：关闭；)")
    @Excel(name = "同包装编码状态(1:激活;2:失效;3：关闭；)", height = 20, width = 30) 
    @Column(name = "same_package_code_status")
    private Byte samePackageCodeStatus;

    /**
     * 同包装编码数量
     */
    @ApiModelProperty(name="samePackageCodeQty",value = "同包装编码数量")
    @Excel(name = "同包装编码数量", height = 20, width = 30) 
    @Column(name = "same_package_code_qty")
    private BigDecimal samePackageCodeQty;

    /**
     * 已匹配数量
     */
    @ApiModelProperty(name="matchedQty",value = "已匹配数量")
    @Excel(name = "已匹配数量", height = 20, width = 30) 
    @Column(name = "matched_qty")
    private BigDecimal matchedQty;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30) 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30) 
    @Column(name = "org_id")
    private Long orgId;

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

    private static final long serialVersionUID = 1L;
}