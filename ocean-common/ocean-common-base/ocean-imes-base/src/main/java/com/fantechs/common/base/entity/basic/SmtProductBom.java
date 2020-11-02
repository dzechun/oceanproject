package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @ApiModelProperty(name="productBomId" ,value="产品BOM ID")
    @NotNull(groups = update.class,message = "产品BOM ID不能为空")
    private Long productBomId;

    /**
     * 物料清单编号
     */
    @Column(name = "product_bom_code")
    @ApiModelProperty(name="productBomCode" ,value="BOM ID")
    @Excel(name = "BOM ID", height = 20, width = 30)
    @NotBlank(message = "物料清单编码不能为空")
    private String productBomCode;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30)
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name="takt" ,value="节拍数量(秒)")
    private Integer takt;

    /**
     * 移转数量
     */
    @Column(name = "transfer_quantity")
    @ApiModelProperty(name="transferQuantity" ,value="移转数量")
    private Integer transferQuantity;

    /**
     * BOM状态(1-未核准 2-已核准)
     */
    @Column(name = "bom_status")
    @ApiModelProperty(name="bomStatus" ,value="BOM状态(1-未核准 2-已核准)")
    private Byte bomStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态（0、无效 1、有效）")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 父BOM ID
     */
    @Column(name = "parent_bom_id")
    @ApiModelProperty(name="materialId" ,value="父BOM ID")
    private Long parentBomId;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
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


    @Transient
    private List<SmtProductBom> children;
}