package com.fantechs.common.base.general.entity.basic;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "base_route_process")
@Data
public class BaseRouteProcess extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 5370182907126666486L;
    /**
     * 工艺路线与工艺关系ID
     */
    @Id
    @Column(name = "route_process_id")
    @ApiModelProperty(name="routeProcessId" ,value="工艺路线与工艺关系ID")
    @NotNull(groups = update.class,message = "工艺路线与工艺关系ID不能为空")
    private Long routeProcessId;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    @NotNull(message = "工艺路线id不能为空")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 工序类别编码
     */
    @Transient
    @ApiModelProperty(name="processCategoryCode" ,value="工序类别编码")
    private String processCategoryCode;

    /**
     * 工段ID
     */
    @Column(name = "section_id")
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    @NotNull(message = "工段id不能为空")
    private Long sectionId;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name="sectionName" ,value="工段名称")
    private String sectionName;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name="processId" ,value="工序ID")
    @NotNull(message = "工序id不能为空")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

    /**
     * 下一道工序ID
     */
    @Column(name = "next_process_id")
    @ApiModelProperty(name="nextProcessId" ,value="下一道工序ID")
    private Long nextProcessId;

    /**
     * 下一道工序名称
     */
    @Transient
    @ApiModelProperty(name="nextProcessName" ,value="下一道工序名称")
    private String nextProcessName;

    /**
     * 上一道工序ID
     */
    @Column(name = "previous_process_id")
    @ApiModelProperty(name="previousProcessId" ,value="上一道工序ID")
    private Long previousProcessId;

    /**
     * 上一道工序名称
     */
    @Transient
    @ApiModelProperty(name="previousProcessName" ,value="上一道工序名称")
    private String previousProcessName;

    /**
     * 工序顺序
     */
    @Column(name = "order_num")
    @ApiModelProperty(name="orderNum" ,value="工序顺序")
    private Integer orderNum;

    /**
     * 是否通过(0.否 1.是)
     */
    @Column(name = "is_pass")
    @ApiModelProperty(name="isPass" ,value="是否通过(0.否 1.是)")
    private Integer isPass;

    /**
     * 是否必过工序(0.否/N 1.是/Y)
     */
    @Column(name = "is_must_pass")
    @ApiModelProperty(name="isMustPass" ,value="是否必过工序(0.否/N 1.是/Y)")
    private Integer isMustPass;

    /**
     * 检验时间(秒)
     */
    @Column(name = "inspection_time")
    @ApiModelProperty(name="inspectionTime" ,value="检验时间(秒)")
    private Integer inspectionTime;

    /**
     * 资质ID
     */
    @Column(name = "qualification_id")
    @ApiModelProperty(name="qualificationId" ,value="资质ID")
    private Long qualificationId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 标准时间
     */
    @Column(name = "standard_time")
    @ApiModelProperty(name="standardTime" ,value="标准时间")
    private Integer standardTime;

    /**
     * 准备时间
     */
    @Column(name = "readiness_time")
    @ApiModelProperty(name="readinessTime" ,value="准备时间")
    private Integer readinessTime;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    @ApiModelProperty(name="processCode",value = "工序编码")
    @Transient
    private String processCode;

}
