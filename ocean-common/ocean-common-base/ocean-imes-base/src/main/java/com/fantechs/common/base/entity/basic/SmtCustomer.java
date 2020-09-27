package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_customer")
@Data
public class SmtCustomer implements Serializable {

    private static final long serialVersionUID = 4936567779340297991L;
    /**
     * 客户ID
     */
    @Id
    @Column(name = "customer_id")
    @ApiModelProperty(name="customerId" ,value="客户ID")
    private Long customerId;

    /**
     * 客户代码
     */
    @Column(name = "customer_code")
    @ApiModelProperty(name="customerCode" ,value="客户代码")
    private String customerCode;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;

    /**
     * 客户描述
     */
    @Column(name = "customer_desc")
    @ApiModelProperty(name="customerDesc" ,value="客户描述")
    private String customerDesc;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

}