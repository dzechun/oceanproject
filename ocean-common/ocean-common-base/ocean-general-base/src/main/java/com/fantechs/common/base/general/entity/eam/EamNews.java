package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 作业指导书-新闻
 * eam_news
 * @author admin
 * @date 2021-07-07 09:33:31
 */
@Data
@Table(name = "eam_news")
public class EamNews extends ValidGroup implements Serializable {
    /**
     * 新闻ID
     */
    @ApiModelProperty(name="newsId",value = "新闻ID")
    @Id
    @Column(name = "news_id")
    @NotNull(groups = update.class,message = "新闻ID不能为空")
    private Long newsId;

    /**
     * 新闻编码
     */
    @ApiModelProperty(name="newsCode",value = "新闻编码")
    @Excel(name = "新闻编码", height = 20, width = 30,orderNum="1")
    @Column(name = "news_code")
    private String newsCode;

    /**
     * 新闻标题
     */
    @ApiModelProperty(name="newsTitle",value = "新闻标题")
    @Excel(name = "新闻标题", height = 20, width = 30,orderNum="3")
    @Column(name = "news_title")
    @NotBlank(message = "新闻标题不能为空")
    private String newsTitle;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workShopId",value = "车间ID")
    @Column(name = "work_shop_id")
    private Long workShopId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 新闻状态(1-待审核 2-已审核 3-已发布)
     */
    @ApiModelProperty(name="newStatus",value = "新闻状态(1-待审核 2-已审核 3-已发布)")
    @Excel(name = "新闻状态(1-待审核 2-已审核 3-已发布)", height = 20, width = 30,orderNum="7")
    @Column(name = "new_status")
    private Byte newStatus;

    /**
     * 审核用户ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核用户ID")
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
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="8")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 新闻附件
     */
    @ApiModelProperty(name="list",value = "新闻附件")
    private List<EamNewsAttachment> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}