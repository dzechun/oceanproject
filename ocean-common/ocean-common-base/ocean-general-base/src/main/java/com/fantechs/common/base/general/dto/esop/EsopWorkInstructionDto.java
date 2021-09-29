package com.fantechs.common.base.general.dto.esop;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.esop.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EsopWorkInstructionDto extends EsopWorkInstruction implements Serializable {

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4")
    @Transient
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Transient
    private String materialName;

    /**
     * 物料规格
     */
    @ApiModelProperty(name="materialDesc",value = "物料规格")
    @Transient
    private String materialDesc;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="productModelName",value = "产品名称")
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
     * 产品规格
     */
    @ApiModelProperty(name="productModelDesc",value = "产品规格")
    @Transient
    private String productModelDesc;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="7")
    @Transient
    private String processName;

    /**
     * 工序编号
     */
    @ApiModelProperty(name="processCode",value = "工序编号")
    @Transient
    private String processCode;

    @Transient
    private List<EsopWiBom> esopWiBoms;

    @Transient
    private List<EsopWiFTAndInspectionTool> esopWiFTAndInspectionTools;

    @Transient
    private List<EsopWiQualityStandards> esopWiQualityStandardss;

    @Transient
    private List<EsopWiFile> esopWiFiles;

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

    /**
     * 体积
     */
    @ApiModelProperty(name="specifications",value = "体积")
    @Transient
    private String specifications;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    @Transient
    private String proName;


    /**
     * 当前登录用户
     */
    @ApiModelProperty(name="userName",value = "当前登录用户")
    @Transient
    private String userName;

    /**
     * 车间名称
     */
    @Transient
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;
}
