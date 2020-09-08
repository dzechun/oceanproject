package com.fantechs.common.base.entity.sysmanage.history;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "smt_ht_pro_line")
@Data
public class SmtHtProLine {
    /**
     * 线别ID
     */
    @Id
    @Column(name = "ht_pro_line_id")
    private String htProLineId;

    /**
     * 线别代码
     */
    @Column(name = "pro_code")
    private String proCode;

    /**
     * 线别名称
     */
    @Column(name = "pro_name")
    private String proName;

    /**
     * 线别描述
     */
    @Column(name = "pro_desc")
    private String proDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    private String factoryId;

    /**
     * 厂别名称
     */
    @Transient
    private String factoryName;

    /**
     * 车间ID
     */
    @Column(name = "work_shop_id")
    private String workShopId;

    /**
     * 车间名称
     */
    @Transient
    private String workShopName;

    /**
     * 产线状态（0、无效 1、有效）
     */
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    private String modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    private Date modifiedTime;

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