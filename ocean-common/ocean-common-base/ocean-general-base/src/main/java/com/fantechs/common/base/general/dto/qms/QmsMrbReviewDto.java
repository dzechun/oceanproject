package com.fantechs.common.base.general.dto.qms;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class QmsMrbReviewDto extends QmsMrbReview implements Serializable {


    /**
     * 供应商
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商")
    @Excel(name = "供应商", height = 20, width = 30,orderNum="2")
    private String supplierName;

    /**
     * 质检单号
     */
    @Transient
    @ApiModelProperty(name="qualityInspectionCode",value = "质检单号")
    @Excel(name = "质检单号", height = 20, width = 30,orderNum="3")
    private String qualityInspectionCode;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 评审部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "评审部门名称")
    @Excel(name = "评审部门", height = 20, width = 30,orderNum="4")
    private String deptName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
