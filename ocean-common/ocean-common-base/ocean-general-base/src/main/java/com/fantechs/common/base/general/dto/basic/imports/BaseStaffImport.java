package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseStaffImport implements Serializable {

    /**
     * 员工编码
     */
    @ApiModelProperty(name="staffCode",value = "员工编码")
    @Excel(name = "员工编码(必填)", height = 20, width = 30)
    private String staffCode;

    /**
     * 员工名称
     */
    @ApiModelProperty(name="staffName",value = "员工名称")
    @Excel(name = "员工名称", height = 20, width = 30)
    private String staffName;

    /**
     * 员工描述
     */
    @ApiModelProperty(name="staffDesc",value = "员工描述")
    @Excel(name = "员工描述", height = 20, width = 30)
    private String staffDesc;

    /**
     * 班组ID
     */
    @ApiModelProperty(name="teamId",value = "班组ID")
    private Long teamId;

    /**
     * 班组代码
     */
    @ApiModelProperty(name="teamCode",value = "班组代码")
    @Excel(name = "班组编码", height = 20, width = 30)
    private String teamCode;

    /**
     * 工种ID
     */
    @ApiModelProperty(name="processId",value = "工种ID")
    private Long processId;

    /**
     * 工种编码
     */
    @ApiModelProperty(name="processCode",value = "工种编码")
    @Excel(name = "工种编码(必填)", height = 20, width = 30)
    private String processCode;

    /**
     * 有效开始时间
     */
    @ApiModelProperty(name="effectiveStartTime",value = "有效开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效开始时间", height = 20, width = 30,importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;

    /**
     * 有效结束时间
     */
    @ApiModelProperty(name="effectiveEndTime",value = "有效结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效结束时间", height = 20, width = 30,importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;

    /**
     * 身份证号
     */
    @ApiModelProperty(name="identityNumber",value = "身份证号")
    @Excel(name = "身份证号", height = 20, width = 30)
    private String identityNumber;

    /**
     * 是否团队计件（0、否 1、是）
     */
    @ApiModelProperty(name="isTeamPiecework",value = "是否团队计件（0、否 1、是）")
    @Excel(name = "是否团队计件（0、否 1、是）", height = 20, width = 30)
    private Integer isTeamPiecework;

    /**
     * 员工状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "员工状态（0、不启用 1、启用）")
    @Excel(name = "员工状态（0、不启用 1、启用）", height = 20, width = 30)
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
