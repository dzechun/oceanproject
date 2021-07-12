package com.fantechs.common.base.general.entity.eam;

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
    @Excel(name = "ESOP发布管理ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "wi_release_id")
    private Long wiReleaseId;

    /**
     * 发布编码
     */
    @ApiModelProperty(name="wiReleaseCode",value = "发布编码")
    @Excel(name = "发布编码", height = 20, width = 30,orderNum="1")
    @Column(name = "wi_release_code")
    private String wiReleaseCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Excel(name = "工艺路线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Excel(name = "车间ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Excel(name = "工厂ID", height = 20, width = 30,orderNum="") 
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 发布状态(1-待发布 2-已发布)
     */
    @ApiModelProperty(name="release_status",value = "发布状态(1-待发布 2-已发布)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="11")
    private Byte releaseStatus;

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
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="12")
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
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="14")
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