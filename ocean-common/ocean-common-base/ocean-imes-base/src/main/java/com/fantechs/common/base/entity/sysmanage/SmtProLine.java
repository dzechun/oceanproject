package com.fantechs.common.base.entity.sysmanage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_pro_line")
@Data
public class SmtProLine implements Serializable {

    private static final long serialVersionUID = -7775679129014320519L;
    /**
     * 线别ID
     */
    @Id
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 线别代码
     */
    @Column(name = "pro_code")
    @Excel(name = "线别代码", height = 20, width = 30)
    private String proCode;

    /**
     * 线别名称
     */
    @Column(name = "pro_name")
    @Excel(name = "线别名称", height = 20, width = 30)
    private String proName;

    /**
     * 线别描述
     */
    @Column(name = "pro_desc")
    @Excel(name = "线别描述", height = 20, width = 30)
    private String proDesc;

    /**
     * 厂别ID
     */
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 厂别名称
     */
    @Transient
    @Excel(name = "厂别名称", height = 20, width = 30)
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
    @Excel(name = "车间名称", height = 20, width = 30)
    private String workShopName;

    /**
     * 产线状态（0、无效 1、有效）
     */
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建账号
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @Excel(name = "修改账号", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @Excel(name = "修改时间", height = 20, width = 30)
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