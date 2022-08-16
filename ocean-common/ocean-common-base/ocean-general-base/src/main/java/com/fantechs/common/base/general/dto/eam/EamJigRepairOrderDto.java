package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigRepairOrderDto extends EamJigRepairOrder implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="10")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="12")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="3")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="4")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="5")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="6")
    private String jigModel;

    /**
     * 不良原因编码
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseCode",value = "不良原因编码")
    private String badnessCauseCode;

    /**
     * 不良原因描述
     */
    @Transient
    @ApiModelProperty(name = "badnessCauseDesc",value = "不良原因描述")
    private String badnessCauseDesc;

    /**
     * 不良责任编码
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyCode",value = "不良责任编码")
    private String badnessDutyCode;

    /**
     * 不良责任描述
     */
    @Transient
    @ApiModelProperty(name = "badnessDutyDesc",value = "不良责任描述")
    private String badnessDutyDesc;

    /**
     * 不良现象编码
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeCode",value = "不良现象编码")
    private String badnessPhenotypeCode;

    /**
     * 不良现象描述
     */
    @Transient
    @ApiModelProperty(name = "badnessPhenotypeDesc",value = "不良现象描述")
    private String badnessPhenotypeDesc;

    /**
     * 维修人员
     */
    @Transient
    @ApiModelProperty(name = "repairUserName",value = "维修人员")
    @Excel(name = "维修人员", height = 20, width = 30,orderNum="9")
    private String repairUserName;

}
