package com.fantechs.common.base.general.entity.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 材料维护日志
 * eng_material_maintain_log
 * @author jbb
 * @date 2021-11-04 09:23:07
 */
@Data
@Table(name = "eng_material_maintain_log")
public class EngMaterialMaintainLog extends ValidGroup implements Serializable {
    /**
     * 材料维护日志ID
     */
    @ApiModelProperty(name="materialMaintainLogId",value = "材料维护日志ID")
    @Excel(name = "材料维护日志ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "material_maintain_log_id")
    private Long materialMaintainLogId;

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    @Excel(name = "合同量单ID", height = 20, width = 30,orderNum="")
    @Column(name = "contract_qty_order_id")
    private Long contractQtyOrderId;

    /**
     * 维护信息
     */
    @ApiModelProperty(name="maintainContent",value = "维护信息")
    @Excel(name = "维护信息", height = 20, width = 30,orderNum="")
    @Column(name = "maintain_content")
    private String maintainContent;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作用户ID")
    @Excel(name = "操作用户ID", height = 20, width = 30,orderNum="")
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

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

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "option1")
    @Excel(name = "option1", height = 20, width = 30,orderNum="")
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2",value = "option2")
    @Excel(name = "option2", height = 20, width = 30,orderNum="")
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3",value = "option3")
    @Excel(name = "option3", height = 20, width = 30,orderNum="")
    private String option3;

    private static final long serialVersionUID = 1L;
}
