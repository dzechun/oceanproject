package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.history.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EamHtWorkInstructionDto extends EamHtWorkInstruction implements Serializable {

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="productModelName",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="5")
    @Transient
    private String productModelName;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="6")
    @Transient
    private String productModelCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="7")
    @Transient
    private String processName;

    @Transient
    private List<EamHtWiBom> eamHtWiBoms;

    @Transient
    private List<EamHtWiFTAndInspectionTool> eamHtWiFTAndInspectionTools;

    @Transient
    private List<EamHtWiQualityStandards> eamHtWiQualityStandardss;

    @Transient
    private List<EamHtWiFile> eamHtWiFiles;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    @Transient
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName",value = "修改人名称")
    @Transient
    private String modifiedUserName;
}
