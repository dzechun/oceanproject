package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 配送点信息表
 * base_storage_task_point
 * @author admin
 * @date 2021-09-09 21:28:02
 */
@Data
@Table(name = "base_storage_task_point")
public class BaseStorageTaskPoint extends ValidGroup implements Serializable {
    /**
     * 库位配送点ID
     */
    @ApiModelProperty(name="storageTaskPointId",value = "库位配送点ID")
    @Id
    @Column(name = "storage_task_point_id")
    @NotNull(groups = update.class,message = "库位配送点ID不能为空")
    private Long storageTaskPointId;

    /**
     * 配送点编码
     */
    @ApiModelProperty(name="taskPointCode",value = "配送点编码")
    @Excel(name = "配送点编码", height = 20, width = 30,orderNum="1")
    @Column(name = "task_point_code")
    @NotBlank(message = "配送点编码不能为空")
    private String taskPointCode;

    /**
     * 配送点名称
     */
    @ApiModelProperty(name="taskPointName",value = "配送点名称")
    @Excel(name = "配送点名称", height = 20, width = 30,orderNum="2")
    @Column(name = "task_point_name")
    @NotBlank(message = "配送点名称不能为空")
    private String taskPointName;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Column(name = "storage_id")
    @NotNull(message = "库位不能为空")
    private Long storageId;

    /**
     * 配送点类型(1-备料 2-存料)
     */
    @ApiModelProperty(name="taskPointType",value = "配送点类型(1-备料 2-存料)")
    @Excel(name = "配送点类型(1-备料 2-存料)", height = 20, width = 30,orderNum="3",replace = {"备料_1","存料_2"})
    @Column(name = "task_point_type")
    private Byte taskPointType;

    /**
     * 坐标编码
     */
    @ApiModelProperty(name="xyzCode",value = "坐标编码")
    @Excel(name = "坐标编码", height = 20, width = 30,orderNum="7")
    @Column(name = "xyz_code")
    @NotBlank(message = "坐标编码不能为空")
    private String xyzCode;

    /**
     * 使用优先级
     */
    @ApiModelProperty(name="usePriority",value = "使用优先级")
    @Excel(name = "使用优先级", height = 20, width = 30,orderNum="8")
    @Column(name = "use_priority")
    @NotNull(message = "使用优先级不能为空")
    private Integer usePriority;

    /**
     * 层级类别
     */
    @ApiModelProperty(name="hierarchicalCategory",value = "层级类别")
    @Column(name = "hierarchical_category")
    private String hierarchicalCategory;

    /**
     * 配送方式
     */
    @ApiModelProperty(name="type",value = "配送方式")
    @Excel(name = "配送方式", height = 20, width = 30,orderNum="9")
    @Column(name = "type")
    private String type;

    /**
     * 库位配送点状态(1-空闲 2-使用)
     */
    @ApiModelProperty(name="storageTaskPointStatus",value = "库位配送点状态(1-空闲 2-使用)")
    @Excel(name = "库位配送点状态(1-空闲 2-使用)", height = 20, width = 30,orderNum="10")
    @Column(name = "storage_task_point_status")
    private Byte storageTaskPointStatus;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name="warehouseAreaId",value = "仓库区域ID")
    @Column(name = "warehouse_area_id")
    private Long warehouseAreaId;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区ID")
    @Column(name = "working_area_id")
    private Long workingAreaId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="11")
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

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="6")
    private String warehouseName;

    /**
     * 库区名称
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaName" ,value="库区名称")
    @Excel(name = "库区名称", height = 20, width = 30,orderNum="5")
    private String warehouseAreaName;

    /**
     * 工作区名称
     */
    @Transient
    @ApiModelProperty(name="workingAreaName" ,value="工作区名称")
    private String workingAreaName;

    /**
     * 仓库编码
     */
    @Transient
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    /**
     * 库区编码
     */
    @Transient
    @ApiModelProperty(name="warehouseAreaCode" ,value="库区编码")
    private String warehouseAreaCode;

    /**
     * 工作区编码
     */
    @Transient
    @ApiModelProperty(name="workingAreaCode" ,value="工作区编码")
    private String workingAreaCode;

    /**
     * 库位编码
     */
    @Transient
    @ApiModelProperty(name="storageCode" ,value="库位编码")
    @Excel(name = "库位编码", height = 20, width = 30,orderNum="4")
    private String storageCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum = "12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 周转工具ID
     */
    @Transient
    @ApiModelProperty(name = "vehicleId",value = "周转工具ID")
    private Long vehicleId;

    /**
     * 周转工具编码
     */
    @Transient
    @ApiModelProperty(name = "vehicleCode",value = "周转工具编码")
    private String vehicleCode;

    /**
     * 周转工具名称
     */
    @Transient
    @ApiModelProperty(name = "vehicleName",value = "周转工具名称")
    private String vehicleName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}