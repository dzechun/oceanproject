package com.fantechs.common.base.general.entity.wms.inner;

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
 * 作业单关联栈板表
 * wms_inner_job_order_re_mspp
 * @author mr.lei
 * @date 2021-05-25 11:38:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wms_inner_job_order_re_mspp")
public class WmsInnerJobOrderReMspp extends ValidGroup implements Serializable {
    /**
     * 作业单关联栈板表ID
     */
    @ApiModelProperty(name="jobOrderReMsppId",value = "作业单关联栈板表ID")
    @Excel(name = "作业单关联栈板表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "job_order_re_mspp_id")
    private Long jobOrderReMsppId;

    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    @Excel(name = "作业单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 车间管理模块栈板表ID
     */
    @ApiModelProperty(name="productPalletId",value = "车间管理模块栈板表ID")
    @Excel(name = "车间管理模块栈板表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_pallet_id")
    private Long productPalletId;

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