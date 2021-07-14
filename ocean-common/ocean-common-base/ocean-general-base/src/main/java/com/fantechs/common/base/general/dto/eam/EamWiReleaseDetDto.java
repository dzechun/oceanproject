package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamWiReleaseDetDto extends EamWiReleaseDet implements Serializable {

    /**
     * WI编码
     */
    @ApiModelProperty(name="workInstructionCode",value = "WI编码")
    @Transient
    private String workInstructionCode;

    /**
     * WI名称
     */
    @ApiModelProperty(name="workInstructionName",value = "WI名称")
    @Transient
    private String workInstructionName;

    /**
     * WI版本
     */
    @ApiModelProperty(name="workInstructionVer",value = "WI版本")
    @Transient
    private String workInstructionVer;


    /**
     * 所属工序ID
     */
    @ApiModelProperty(name="processId",value = "所属工序ID")
    @Transient
    private Long processId;


    /**
     * 所属工序名称
     */
    @ApiModelProperty(name="processName",value = "所属工序名称")
    @Transient
    private String processName;
}
