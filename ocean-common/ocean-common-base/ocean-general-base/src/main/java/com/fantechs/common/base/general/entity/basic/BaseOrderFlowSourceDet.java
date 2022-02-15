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
import java.util.Date;

;
;

/**
 * 单据流配置
 * base_order_flow_source_det
 * @author admin
 * @date 2022-02-15 09:53:41
 */
@Data
@Table(name = "base_order_flow_source_det")
public class BaseOrderFlowSourceDet extends ValidGroup implements Serializable {
    /**
     * 单据流配置源明细ID
     */
    @ApiModelProperty(name="orderFlowSourceDetId",value = "单据流配置源明细ID")
    @Id
    @Column(name = "order_flow_source_det_id")
    private Long orderFlowSourceDetId;

    /**
     * 单据流配置源ID
     */
    @ApiModelProperty(name="orderFlowSourceId",value = "单据流配置源ID")
    @Column(name = "order_flow_source_id")
    private Long orderFlowSourceId;

    /**
     * 来源单据类型编码
     */
    @ApiModelProperty(name="sourceOrderTypeCode",value = "来源单据类型编码")
    @Excel(name = "来源单据类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "source_order_type_code")
    private String sourceOrderTypeCode;

    /**
     * 下推单据类型编码
     */
    @ApiModelProperty(name="nextOrderTypeCode",value = "下推单据类型编码")
    @Excel(name = "下推单据类型编码", height = 20, width = 30,orderNum="2")
    @Column(name = "next_order_type_code")
    private String nextOrderTypeCode;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
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
    //@Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    //@Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}