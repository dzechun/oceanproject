package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 配套信息表
 * mes_pm_matching
 * @author 53203
 * @date 2021-02-02 15:49:37
 */
@Data
@Table(name = "mes_pm_matching")
public class MesPmMatching extends ValidGroup implements Serializable {
    /**
     * 配套信息ID
     */
    @ApiModelProperty(name="matchingId",value = "配套信息ID")
    @Excel(name = "配套信息ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "matching_id")
    private Long matchingId;

    /**
     * 产品工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "产品工单ID")
    @Excel(name = "产品工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQuantity",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_quantity")
    private BigDecimal workOrderQuantity;

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQuantity",value = "投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="") 
    @Column(name = "production_quantity")
    private BigDecimal productionQuantity;

    /**
     * 已配套数量
     */
    @ApiModelProperty(name="alreadyMatchingQuantity",value = "已配套数量")
    @Excel(name = "已配套数量", height = 20, width = 30,orderNum="") 
    @Column(name = "already_matching_quantity")
    private BigDecimal alreadyMatchingQuantity;

    /**
     * 最小齐套数
     */
    @ApiModelProperty(name="minMatchingQuantity",value = "最小齐套数")
    @Excel(name = "最小齐套数", height = 20, width = 30,orderNum="") 
    @Column(name = "min_matching_quantity")
    private BigDecimal minMatchingQuantity;

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
    @Column(name = "org_id")
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
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}