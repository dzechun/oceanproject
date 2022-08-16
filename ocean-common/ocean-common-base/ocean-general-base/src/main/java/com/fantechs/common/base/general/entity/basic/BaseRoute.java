package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "base_route")
@Data
public class BaseRoute extends ValidGroup implements Serializable {

    private static final long serialVersionUID = -2385571776152019549L;
    /**
     * 工艺路线ID
     */
    @Id
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    @NotNull(groups = update.class,message = "工艺路线id不能为空")
    private Long routeId;

    /**
     * 工艺路线编码
     */
    @Column(name = "route_code")
    @ApiModelProperty(name="routeCode" ,value="工艺路线代码")
    @Excel(name = "工艺路线编码", height = 20, width = 30)
    @NotBlank(message = "工艺路线编码不能为空")
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @Column(name = "route_name")
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30)
    @NotBlank(message = "工艺路线名称不能为空")
    private String routeName;

    /**
     * 工艺路线描述
     */
    @Column(name = "route_desc")
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
    @Excel(name = "工艺路线描述", height = 20, width = 30)
    private String routeDesc;

    /**
     * 工艺路线类型（1、成品 2、半成品 3、部件）
     */
    @ApiModelProperty(name="routeType" ,value="工艺路线类型")
    @Excel(name = "工艺路线类型", height = 20, width = 30,replace = {"成品_1", "半成品_2", "部件_3"})
    @Column(name = "route_type")
    private Integer routeType;

    /**
     * 标准时间
     */
    @Column(name = "standard_time")
    @ApiModelProperty(name="standardTime" ,value="标准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date standardTime;

    /**
     * 准备时间
     */
    @Column(name = "readiness_time")
    @ApiModelProperty(name="readinessTime" ,value="准备时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readinessTime;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name="createUserId" ,value="创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name="modifiedUserId" ,value="修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name="isDelete" ,value="逻辑删除")
    private Byte isDelete;

    /**
     * 工序ID
     */
    @Transient
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 工序代码
     */
    @Transient
    @ApiModelProperty(name="processCode" ,value="工序代码")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 工序描述
     */
    @Transient
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    private String processDesc;

    @Transient
    @ApiModelProperty(name = "",notes = "产品工艺路线")
    private BaseProductProcessRoute baseProductProcessRoute;

    @Transient
    @ApiModelProperty(name = "工艺路线工序关系集合",notes = "工艺路线工序关系集合")
    private List<BaseRouteProcess> baseRouteProcesses;

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
