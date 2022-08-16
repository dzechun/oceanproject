package com.fantechs.common.base.general.entity.basic;

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
 * 产品良率设置
 * base_product_yield
 * @author 81947
 * @date 2021-10-20 10:02:09
 */
@Data
@Table(name = "base_product_yield")
public class BaseProductYield extends ValidGroup implements Serializable {
    /**
     * 产品良率设置ID
     */
    @ApiModelProperty(name="productYieldId",value = "产品良率设置ID")
    @Id
    @Column(name = "product_yield_id")
    private Long productYieldId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 停线良率
     */
    @ApiModelProperty(name="productlineStopYield",value = "停线良率")
    @Excel(name = "停线良率", height = 20, width = 30,orderNum="6")
    @Column(name = "productline_stop_yield")
    private BigDecimal productlineStopYield;

    /**
     * 预警良率
     */
    @ApiModelProperty(name="warningYield",value = "预警良率")
    @Excel(name = "预警良率", height = 20, width = 30,orderNum="7")
    @Column(name = "warning_yield")
    private BigDecimal warningYield;

    /**
     * 良率类别(1-通用 2-产线 3-料号)
     */
    @ApiModelProperty(name="yieldType",value = "良率类别(1-通用 2-产线 3-料号)")
    @Excel(name = "良率类别(1-通用 2-产线 3-料号)", height = 20, width = 30,orderNum="1")
    @Column(name = "yield_type")
    private Byte yieldType;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}