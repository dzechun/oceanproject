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
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 作业指导书-新闻履历表
 * eam_ht_news
 * @author admin
 * @date 2021-07-07 09:33:32
 */
@Data
@Table(name = "eam_ht_news")
public class EamHtNews extends ValidGroup implements Serializable {
    /**
     * 新闻履历ID
     */
    @ApiModelProperty(name="htNewsId",value = "新闻履历ID")
    @Excel(name = "新闻履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_news_id")
    private Long htNewsId;

    /**
     * 新闻ID
     */
    @ApiModelProperty(name="newsId",value = "新闻ID")
    @Excel(name = "新闻ID", height = 20, width = 30,orderNum="") 
    @Column(name = "news_id")
    private Long newsId;

    /**
     * 新闻编码
     */
    @ApiModelProperty(name="newsCode",value = "新闻编码")
    @Excel(name = "新闻编码", height = 20, width = 30,orderNum="") 
    @Column(name = "news_code")
    private String newsCode;

    /**
     * 新闻名称
     */
    @ApiModelProperty(name="newsName",value = "新闻名称")
    @Excel(name = "新闻名称", height = 20, width = 30,orderNum="2")
    @Column(name = "news_name")
    @NotBlank(message = "新闻名称不能为空")
    private String newsName;

    /**
     * 新闻标题
     */
    @ApiModelProperty(name="newsTitle",value = "新闻标题")
    @Excel(name = "新闻标题", height = 20, width = 30,orderNum="") 
    @Column(name = "news_title")
    private String newsTitle;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Excel(name = "工厂ID", height = 20, width = 30,orderNum="") 
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Excel(name = "车间ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 新闻状态(1-待审核 2-已审核 3-已发布)
     */
    @ApiModelProperty(name="newStatus",value = "新闻状态(1-待审核 2-已审核 3-已发布)")
    @Excel(name = "新闻状态(1-待审核 2-已审核 3-已发布)", height = 20, width = 30,orderNum="") 
    @Column(name = "new_status")
    private Byte newStatus;

    /**
     * 审核用户ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核用户ID")
    @Excel(name = "审核用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核时间
     */
    @ApiModelProperty(name="auditDate",value = "审核时间")
    @Excel(name = "审核时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "audit_date")
    private Date auditDate;

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 工厂名称
     */
    @Transient
    @ApiModelProperty(name = "factoryName",value = "工厂名称")
    @Excel(name = "工厂名称", height = 20, width = 30,orderNum="6")
    private String factoryName;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="6")
    private String workShopName;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="6")
    private String proName;

    /**
     * 审核人
     */
    @Transient
    @ApiModelProperty(name = "auditUserName",value = "审核人")
    @Excel(name = "审核人", height = 20, width = 30,orderNum="6")
    private String auditUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}