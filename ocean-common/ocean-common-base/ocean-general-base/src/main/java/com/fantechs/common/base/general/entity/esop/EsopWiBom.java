package com.fantechs.common.base.general.entity.esop;

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
import java.util.Date;

;
;

/**
 * 作业指导书-物料清单
 * esop_wi_bom
 * @author 81947
 * @date 2021-07-06 14:05:20
 */
@Data
@Table(name = "esop_wi_bom")
public class EsopWiBom extends ValidGroup implements Serializable {
    /**
     * 物料清单ID
     */
    @ApiModelProperty(name="wiBomId",value = "物料清单ID")
    @Id
    @Column(name = "wi_bom_id")
    private Long wiBomId;

    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    @Column(name = "work_instruction_id")
    private Long workInstructionId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 用量
     */
    @ApiModelProperty(name="usageQty",value = "用量")
    @Excel(name = "用量", height = 20, width = 30,orderNum="4")
    @Column(name = "usage_qty")
    private String usageQty;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30 ,orderNum="1")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30 ,orderNum="2")
    @Transient
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    @Excel(name = "物料版本", height = 20, width = 30 ,orderNum="3")
    @Transient
    private String materialVersion;

    /**
     * 物料规格
     */
    @ApiModelProperty(name="materialDesc" ,value="物料规格")
    @Transient
    private String materialDesc;
}