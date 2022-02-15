package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 单据流配置
 * base_order_flow_source
 * @author admin
 * @date 2022-02-15 09:53:40
 */
@Data
@Table(name = "base_order_flow_source")
public class BaseOrderFlowSource extends ValidGroup implements Serializable {
    /**
     * 单据流配置源ID
     */
    @ApiModelProperty(name="orderFlowSourceId",value = "单据流配置源ID")
    @Id
    @Column(name = "order_flow_source_id")
    private Long orderFlowSourceId;

    /**
     * 作业模块(1-入库作业 2-库内作业 3-出库作业)
     */
    @ApiModelProperty(name="operationModule",value = "作业模块(1-入库作业 2-库内作业 3-出库作业)")
    @Excel(name = "作业模块(1-入库作业 2-库内作业 3-出库作业)", height = 20, width = 30,orderNum="1")
    @Column(name = "operation_module")
    private Byte operationModule;

    /**
     * 业务类型
     */
    @ApiModelProperty(name="businessType",value = "业务类型")
    @Excel(name = "业务类型", height = 20, width = 30,orderNum="2")
    @Column(name = "business_type")
    private Byte businessType;

    /**
     * 单据流维度(1-通用 2-供应商 3-物料)
     */
    @ApiModelProperty(name="orderFlowDimension",value = "单据流维度(1-通用 2-供应商 3-物料)")
    //@Excel(name = "单据流维度(1-通用 2-供应商 3-物料)", height = 20, width = 30,orderNum="")
    @Column(name = "order_flow_dimension")
    private Byte orderFlowDimension;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    //@Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    //@Excel(name = "供应商ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 单据节点类型编码
     */
    @ApiModelProperty(name="orderTypeCode",value = "单据节点类型编码")
    @Excel(name = "单据节点类型编码", height = 20, width = 30,orderNum="3")
    @Column(name = "order_type_code")
    private String orderTypeCode;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="4")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="5")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    @ApiModelProperty(name="list",value = "单据流数据源")
    private List<BaseOrderFlowSourceDet> list = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}