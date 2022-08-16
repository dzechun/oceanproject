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

/**
 * 部件组成明细表
 * @date 2021-01-15 15:20:14
 */
@Data
@Table(name = "base_plate_parts_det")
public class BasePlatePartsDet extends ValidGroup implements Serializable {
    /**
     * 部件组成明细ID
     */
    @ApiModelProperty(name="platePartsDetId",value = "部件组成明细ID")
    @Excel(name = "部件组成明细ID", height = 20, width = 30)
    @Id
    @Column(name = "plate_parts_det_id")
    private Long platePartsDetId;

    /**
     * 部件组成ID
     */
    @ApiModelProperty(name="platePartsId",value = "部件组成ID")
    @Excel(name = "部件组成ID", height = 20, width = 30)
    @Column(name = "plate_parts_id")
    private Long platePartsId;

    /**
     * 部件资料ID
     */
    @ApiModelProperty(name="partsInformationId",value = "部件资料ID")
    @Excel(name = "部件资料ID", height = 20, width = 30)
    @Column(name = "parts_information_id")
    private Long partsInformationId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="route_id",value = "工艺路线ID")
    @Excel(name = "工艺路线ID", height = 20, width = 30)
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30)
    private String spec;

    /**
     * 单位
     */
    @ApiModelProperty(name="unit",value = "单位")
    @Excel(name = "单位", height = 20, width = 30)
    private String unit;

    /**
     * 用量
     */
    @ApiModelProperty(name="useQty",value = "用量")
    @Excel(name = "用量", height = 20, width = 30)
    @Column(name = "use_qty")
    private BigDecimal useQty;

    /**
     * 颜色
     */
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30)
    private String color;

    /**
     * 材质
     */
    @ApiModelProperty(name="texture",value = "材质")
    @Excel(name = "材质", height = 20, width = 30)
    @Column(name = "texture")
    private String texture;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
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

    private static final long serialVersionUID = 1L;
}
