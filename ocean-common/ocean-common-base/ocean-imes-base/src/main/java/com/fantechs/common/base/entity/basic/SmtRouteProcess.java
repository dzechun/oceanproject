package com.fantechs.common.base.entity.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Table(name = "smt_route_process")
@Data
public class SmtRouteProcess implements Serializable {
    private static final long serialVersionUID = 5370182907126666486L;
    /**
     * 工艺路线与工艺关系ID
     */
    @Id
    @Column(name = "route_process_id")
    @ApiModelProperty(name="routeProcessId" ,value="工艺路线与工艺关系ID")
    private Long routeProcessId;

    /**
     * 工艺路线ID
     */
    @Column(name = "route_id")
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;
    /**
     * 工段ID
     */
    @Column(name = "section_id")
    @ApiModelProperty(name="sectionId" ,value="工段ID")
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
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    private String processName;

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
    private Byte isPass;

    /**
     * 是否必过工序(0.否/N 1.是/Y)
     */
    @Column(name = "is_must_pass")
    @ApiModelProperty(name="isMustPass" ,value="是否必过工序(0.否/N 1.是/Y)")
    private Byte isMustPass;

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

}