package com.fantechs.common.base.general.entity.wanbao;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

;
;

/**
 * 堆垛信息
 * wanbao_stacking
 * @author bgkun
 * @date 2021-11-29 09:51:36
 */
@Data
@Table(name = "wanbao_stacking")
public class WanbaoStacking extends ValidGroup implements Serializable {
    /**
     * 堆垛ID
     */
    @ApiModelProperty(name="stackingId",value = "堆垛ID")
    @Id
    @Column(name = "stacking_id")
    private Long stackingId;

    /**
     * 堆垛编码
     */
    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    @Excel(name = "堆垛编码", height = 20, width = 30,orderNum="1")
    @Column(name = "stacking_code")
    private String stackingCode;

    /**
     * 堆垛名称
     */
    @ApiModelProperty(name="stackingName",value = "堆垛名称")
    @Excel(name = "堆垛名称", height = 20, width = 30,orderNum="2")
    @Column(name = "stacking_name")
    private String stackingName;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 最大容量
     */
    @ApiModelProperty(name="maxCapacity",value = "最大容量")
    @Excel(name = "最大容量", height = 20, width = 30,orderNum="3")
    @Column(name = "max_capacity")
    private BigDecimal maxCapacity;

    /**
     * 使用状态(1-空闲 2-使用中)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-空闲 2-使用中)")
    @Excel(name = "使用状态", height = 20, width = 30,orderNum="6", replace = {"空闲_1", "使用中_2"})
    @Column(name = "usage_status")
    private Byte usageStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,orderNum="7", replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="8")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    @ApiModelProperty(name="list",value = "条码")
    private List<WanbaoStackingDetDto> list;

    private static final long serialVersionUID = 1L;
}