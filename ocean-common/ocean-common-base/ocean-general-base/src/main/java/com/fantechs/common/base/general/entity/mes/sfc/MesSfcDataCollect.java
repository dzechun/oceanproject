package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车间管理-数据采集
 * mes_sfc_data_collect
 * @author bgkun
 * @date 2021-07-20 16:16:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mes_sfc_data_collect")
public class MesSfcDataCollect extends ValidGroup implements Serializable {
    /**
     * 数据采集ID
     */
    @ApiModelProperty(name="dataCollectId",value = "数据采集ID")
    @Excel(name = "数据采集ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "data_collect_id")
    private Long dataCollectId;

    /**
     * EAM模块的设备ID
     */
    @ApiModelProperty(name="equipmentId",value = "EAM模块的设备ID")
    @Excel(name = "EAM模块的设备ID", height = 20, width = 30,orderNum="")
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 采集数据
     */
    @ApiModelProperty(name="collectData",value = "采集数据")
    @Excel(name = "采集数据", height = 20, width = 30,orderNum="")
    @Column(name = "collect_data")
    private String collectData;

    /**
     * 采集时间
     */
    @ApiModelProperty(name="collectTime",value = "采集时间")
    @Excel(name = "采集时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "collect_time")
    private Date collectTime;

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

    private static final long serialVersionUID = 1L;
}