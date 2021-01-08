package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import sun.java2d.pipe.ValidatePipe;

/**
 * 页签信息表
 * base_tab
 * @author 53203
 * @date 2021-01-08 10:28:50
 */
@Data
@Table(name = "base_tab")
public class BaseTab extends ValidGroup implements Serializable {
    /**
     * 页签ID
     */
    @ApiModelProperty(name="tabId",value = "页签ID")
    @Excel(name = "页签ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "tab_id")
    @NotNull(groups = update.class,message = "页签ID不能为空")
    private Long tabId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @ApiModelProperty(name="materialProperty",value = "物料属性(0.半成品，1.成品)")
    @Excel(name = "物料属性(0.半成品，1.成品)", height = 20, width = 30,orderNum="") 
    @Column(name = "material_property")
    private Integer materialProperty;

    /**
     * 图片
     */
    @ApiModelProperty(name="image",value = "图片")
    @Excel(name = "图片", height = 20, width = 30,orderNum="") 
    private String image;

    /**
     * 是否批次(0.否 1.是)
     */
    @ApiModelProperty(name="isBatch",value = "是否批次(0.否 1.是)")
    @Excel(name = "是否批次(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_batch")
    private Integer isBatch;

    /**
     * 是否质量检查(0.否 1.是)
     */
    @ApiModelProperty(name="isQualityTest",value = "是否质量检查(0.否 1.是)")
    @Excel(name = "是否质量检查(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_quality_test")
    private Integer isQualityTest;

    /**
     * 是否箱码(0.否 1.是)
     */
    @ApiModelProperty(name="isCaseCode",value = "是否箱码(0.否 1.是)")
    @Excel(name = "是否箱码(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_case_code")
    private Integer isCaseCode;

    /**
     * 是否序列码(0.否 1.是)
     */
    @ApiModelProperty(name="isSequenceCode",value = "是否序列码(0.否 1.是)")
    @Excel(name = "是否序列码(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "is_sequence_code")
    private Integer isSequenceCode;

    /**
     * 发料方式(0.直领 1.倒冲)
     */
    @ApiModelProperty(name="issueMethod",value = "发料方式(0.直领 1.倒冲)")
    @Excel(name = "发料方式(0.直领 1.倒冲)", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_method")
    private Integer issueMethod;

    /**
     * 是否组合板(0.否 1.是)
     */
    @ApiModelProperty(name="ifCompoboard",value = "是否组合板(0.否 1.是)")
    @Excel(name = "是否组合板(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_compoboard")
    private Integer ifCompoboard;

    /**
     * 是否连板(0.否 1.是)
     */
    @ApiModelProperty(name="ifLinkingBoard",value = "是否连板(0.否 1.是)")
    @Excel(name = "是否连板(0.否 1.是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_linking_board")
    private Integer ifLinkingBoard;

    /**
     * 连板数
     */
    @ApiModelProperty(name="linkingBoardNumber",value = "连板数")
    @Excel(name = "连板数", height = 20, width = 30,orderNum="") 
    @Column(name = "linking_board_number")
    private Integer linkingBoardNumber;

    /**
     * 节拍数量(秒)
     */
    @ApiModelProperty(name="takt",value = "节拍数量(秒)")
    @Excel(name = "节拍数量(秒)", height = 20, width = 30,orderNum="") 
    private Integer takt;

    /**
     * 移转数量
     */
    @ApiModelProperty(name="transferQuantity",value = "移转数量")
    @Excel(name = "移转数量", height = 20, width = 30,orderNum="") 
    @Column(name = "transfer_quantity")
    private Integer transferQuantity;

    /**
     * 最小安全库存
     */
    @ApiModelProperty(name="minSafetyStock",value = "最小安全库存")
    @Excel(name = "最小安全库存", height = 20, width = 30,orderNum="") 
    @Column(name = "min_safety_stock")
    private Integer minSafetyStock;

    /**
     * 最大安全库存
     */
    @ApiModelProperty(name="maxSafetyStock",value = "最大安全库存")
    @Excel(name = "最大安全库存", height = 20, width = 30,orderNum="") 
    @Column(name = "max_safety_stock")
    private Integer maxSafetyStock;

    /**
     * 采购周期
     */
    @ApiModelProperty(name="purchaseCycle",value = "采购周期")
    @Excel(name = "采购周期", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_cycle")
    private Long purchaseCycle;

    /**
     * 主单位
     */
    @ApiModelProperty(name="mainUnit",value = "主单位")
    @Excel(name = "主单位", height = 20, width = 30,orderNum="") 
    @Column(name = "main_unit")
    private String mainUnit;

    /**
     * 辅单位
     */
    @ApiModelProperty(name="subUnit",value = "辅单位")
    @Excel(name = "辅单位", height = 20, width = 30,orderNum="") 
    @Column(name = "sub_unit")
    private String subUnit;

    /**
     * 换算率
     */
    @ApiModelProperty(name="conversionRate",value = "换算率")
    @Excel(name = "换算率", height = 20, width = 30,orderNum="") 
    @Column(name = "conversion_rate")
    private Integer conversionRate;

    /**
     * 客户料号
     */
    @ApiModelProperty(name="customerMaterialCode",value = "客户料号")
    @Excel(name = "客户料号", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_material_code")
    private String customerMaterialCode;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}