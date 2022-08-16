package com.fantechs.common.base.general.entity.mes.pm.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单工序关系履历表
 * mes_pm_ht_work_order_process_re_wo
 */
@Data
@Table(name = "mes_pm_ht_work_order_process_re_wo")
public class MesPmHtWorkOrderProcessReWo extends ValidGroup implements Serializable {
    /**
     * 工单工序关系履历表ID
     */
    @ApiModelProperty(name="htWorkOrderProcessReWoId",value = "工单工序关系履历表ID")
    @Excel(name = "工单工序关系履历表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_work_order_process_re_wo_id")
    private Long htWorkOrderProcessReWoId;

    /**
     * 工单工序关系表ID
     */
    @ApiModelProperty(name="workOrderProcessReWoId",value = "工单工序关系表ID")
    @Excel(name = "工单工序关系表ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_process_re_wo_id")
    private Long workOrderProcessReWoId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="")
    @Column(name = "process_id")
    private Long processId;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30,orderNum="")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="")
    private String processName;

    /**
     * 工序描述
     */
    @Transient
    @ApiModelProperty(name="processDesc",value = "工序描述")
    @Excel(name = "工序描述", height = 20, width = 30,orderNum="")
    private String processDesc;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name="sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="")
    private String sectionName;

    /**
     * 工序类别名称
     */
    @Transient
    @ApiModelProperty(name="processDesc",value = "工序类别名称")
    @Excel(name = "工序类别", height = 20, width = 30,orderNum="")
    private String processCategoryName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    private String modifiedUserName;
    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(value = "组织名称",example = "组织名称")
    private String organizationName;

    /**
     * 工单物料集合
     */
    private List<MesPmHtWorkOrderMaterialReP> list;

    private static final long serialVersionUID = 1L;
}
