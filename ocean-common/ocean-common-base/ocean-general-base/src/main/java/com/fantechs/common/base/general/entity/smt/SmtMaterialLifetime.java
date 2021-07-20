package com.fantechs.common.base.general.entity.smt;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * MSD材料寿命
 * smt_material_lifetime
 * @author admin
 * @date 2021-07-20 10:49:42
 */
@Data
@Table(name = "smt_material_lifetime")
public class SmtMaterialLifetime extends ValidGroup implements Serializable {
    /**
     * 材料寿命ID
     */
    @ApiModelProperty(name="materialLifetimeId",value = "材料寿命ID")
    @Id
    @Column(name = "material_lifetime_id")
    @NotNull(groups = update.class,message = "材料寿命ID不能为空")
    private Long materialLifetimeId;

    /**
     * MSD湿度等级
     */
    @ApiModelProperty(name="theHumidityLevel",value = "MSD湿度等级")
    @Excel(name = "MSD湿度等级", height = 20, width = 30,orderNum="1")
    @Column(name = "the_humidity_level")
    @NotBlank(message = "MSD湿度等级不能为空")
    private String theHumidityLevel;

    /**
     * 密封条件
     */
    @ApiModelProperty(name="sealingCondition",value = "密封条件")
    @Excel(name = "密封条件", height = 20, width = 30,orderNum="2")
    @Column(name = "sealing_condition")
    private String sealingCondition;

    /**
     * 密封保存时间(年)
     */
    @ApiModelProperty(name="sealHoldingTime",value = "密封保存时间(年)")
    @Excel(name = "密封保存时间(年)", height = 20, width = 30,orderNum="3",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "seal_holding_time")
    private BigDecimal sealHoldingTime;

    /**
     * 拆封条件
     */
    @ApiModelProperty(name="openCondition",value = "拆封条件")
    @Excel(name = "拆封条件", height = 20, width = 30,orderNum="4")
    @Column(name = "open_condition")
    private String openCondition;

    /**
     * 拆封保存时间(小时)
     */
    @ApiModelProperty(name="openHoldTime",value = "拆封保存时间(小时)")
    @Excel(name = "拆封保存时间(小时)", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "open_hold_time")
    private BigDecimal openHoldTime;

    /**
     * 高温烘烤温度(t为temperature的缩写)
     */
    @ApiModelProperty(name="hightTBakeT",value = "高温烘烤温度(t为temperature的缩写)")
    @Excel(name = "高温烘烤温度(t为temperature的缩写)", height = 20, width = 30,orderNum="6")
    @Column(name = "hight_t_bake_t")
    private String hightTBakeT;

    /**
     * 低温烘烤温度(t为temperature的缩写)
     */
    @ApiModelProperty(name="lowTBakeT",value = "低温烘烤温度(t为temperature的缩写)")
    @Excel(name = "低温烘烤温度(t为temperature的缩写)", height = 20, width = 30,orderNum="7")
    @Column(name = "low_t_bake_t")
    private String lowTBakeT;

    /**
     * 低温烘烤温度(小时)
     */
    @ApiModelProperty(name="lowTBakeTime",value = "低温烘烤温度(小时)")
    @Excel(name = "低温烘烤温度(小时)", height = 20, width = 30,orderNum="8")
    @Column(name = "low_t_bake_time")
    private BigDecimal lowTBakeTime;

    /**
     * 高温烘烤温度(小时)
     */
    @ApiModelProperty(name="hightTBakeTime",value = "高温烘烤温度(小时)")
    @Excel(name = "高温烘烤温度(小时)", height = 20, width = 30,orderNum="9")
    @Column(name = "hight_t_bake_time")
    private BigDecimal hightTBakeTime;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
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