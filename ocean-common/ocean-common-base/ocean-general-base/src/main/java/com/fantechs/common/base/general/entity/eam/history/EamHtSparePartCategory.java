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
 * 备用件类别履历表
 * eam_ht_spare_part_category
 * @author admin
 * @date 2021-09-17 16:00:52
 */
@Data
@Table(name = "eam_ht_spare_part_category")
public class EamHtSparePartCategory extends ValidGroup implements Serializable {
    /**
     * 备用件类别履历ID
     */
    @ApiModelProperty(name="htSparePartCategoryId",value = "备用件类别履历ID")
    @Excel(name = "备用件类别履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_spare_part_category_id")
    private Long htSparePartCategoryId;

    /**
     * 备用件类别ID
     */
    @ApiModelProperty(name="sparePartCategoryId",value = "备用件类别ID")
    @Excel(name = "备用件类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_category_id")
    private Long sparePartCategoryId;

    /**
     * 备用件类别编码
     */
    @ApiModelProperty(name="sparePartCategoryCode",value = "备用件类别编码")
    @Excel(name = "备用件类别编码", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_category_code")
    private String sparePartCategoryCode;

    /**
     * 备用件类别名称
     */
    @ApiModelProperty(name="sparePartCategoryName",value = "备用件类别名称")
    @Excel(name = "备用件类别名称", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_category_name")
    private String sparePartCategoryName;

    /**
     * 备用件类别描述
     */
    @ApiModelProperty(name="sparePartCategoryDesc",value = "备用件类别描述")
    @Excel(name = "备用件类别描述", height = 20, width = 30,orderNum="") 
    @Column(name = "spare_part_category_desc")
    private String sparePartCategoryDesc;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
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