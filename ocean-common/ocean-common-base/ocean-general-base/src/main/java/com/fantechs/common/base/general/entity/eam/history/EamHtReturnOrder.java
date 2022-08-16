package com.fantechs.common.base.general.entity.eam.history;

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
 * 设备归还单履历表
 * eam_ht_return_order
 * @author admin
 * @date 2021-06-29 10:35:10
 */
@Data
@Table(name = "eam_ht_return_order")
public class EamHtReturnOrder extends ValidGroup implements Serializable {
    /**
     * 归还单履历ID
     */
    @ApiModelProperty(name="htReturnOrderId",value = "归还单履历ID")
    @Excel(name = "归还单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_return_order_id")
    private Long htReturnOrderId;

    /**
     * 归还单ID
     */
    @ApiModelProperty(name="returnOrderId",value = "归还单ID")
    @Excel(name = "归还单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "return_order_id")
    private Long returnOrderId;

    /**
     * 归还单编码(预留)
     */
    @ApiModelProperty(name="returnOrderCode",value = "归还单编码(预留)")
    @Excel(name = "归还单编码(预留)", height = 20, width = 30,orderNum="") 
    @Column(name = "return_order_code")
    private String returnOrderCode;

    /**
     * 归还人ID
     */
    @ApiModelProperty(name="returnUserId",value = "归还人ID")
    @Excel(name = "归还人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "return_user_id")
    private Long returnUserId;

    /**
     * 归还部门ID
     */
    @ApiModelProperty(name="deptId",value = "归还部门ID")
    @Excel(name = "归还部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 归还时间
     */
    @ApiModelProperty(name="returnTime",value = "归还时间")
    @Excel(name = "归还时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "return_time")
    private Date returnTime;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}