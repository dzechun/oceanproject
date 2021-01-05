package com.fantechs.common.base.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class ProcessListDto implements Serializable {

    private static final long serialVersionUID = 947480814891277953L;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

    /**
     * 下一道工序ID
     */
    @ApiModelProperty(name="nextProcessId" ,value="下一道工序ID")
    private Long nextProcessId;

    /**
     * 上一道工序ID
     */
    @ApiModelProperty(name="previousProcessId" ,value="上一道工序ID")
    private Long previousProcessId;

    /**
     * 工序顺序
     */
    @ApiModelProperty(name="orderNum" ,value="工序顺序")
    private Integer orderNum;

    /**
     * 是否通过(0.否 1.是)
     */
    @ApiModelProperty(name="isPass" ,value="是否通过(0.否 1.是)")
    private Integer isPass;

    /**
     * 是否必过工序(0.否/N 1.是/Y)
     */
    @ApiModelProperty(name="isMustPass" ,value="是否必过工序(0.否/N 1.是/Y)")
    private Integer isMustPass;

    /**
     * 检验时间(秒)
     */
    @ApiModelProperty(name="inspectionTime" ,value="检验时间(秒)")
    private Integer inspectionTime;

    /**
     * 资质ID
     */
    @ApiModelProperty(name="qualificationId" ,value="资质ID")
    private Long qualificationId;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
