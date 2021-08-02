package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 作业指导书-ESOP发布管理
 * eam_wi_release
 * @author 81947
 * @date 2021-07-08 15:42:58
 */
@Data
@Table(name = "eam_wi_release")
public class EamWiRelease extends ValidGroup implements Serializable {
    /**
     * ESOP发布管理ID
     */
    @ApiModelProperty(name="wiReleaseId",value = "ESOP发布管理ID")
    @Id
    @Column(name = "wi_release_id")
    private Long wiReleaseId;

    /**
     * 发布编码
     */
    @ApiModelProperty(name="wiReleaseCode",value = "发布编码")
    @NotNull(message = "发布编码不能为空")
    @Excel(name = "发布编码", height = 20, width = 30,orderNum="1")
    @Column(name = "wi_release_code")
    private String wiReleaseCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @NotNull(message = "工单ID不能为空")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @NotNull(message = "产线ID不能为空")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 发布状态(1-待发布 2-已发布)
     */
    @ApiModelProperty(name="release_status",value = "发布状态(1-待发布 2-已发布)")
    @Excel(name = "状态(1-待发布 2-已发布)", height = 20, width = 30,orderNum="11",replace = {"待发布_1","已发布_2"})
    private Byte releaseStatus;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}