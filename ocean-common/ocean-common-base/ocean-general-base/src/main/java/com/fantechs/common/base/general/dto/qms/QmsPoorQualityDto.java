package com.fantechs.common.base.general.dto.qms;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.qms.QmsPoorQuality;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class QmsPoorQualityDto extends QmsPoorQuality implements Serializable {

    /**
     * 不良项目类型ID
     */
    @Transient
    @ApiModelProperty(name = "badItemId",value = "不良项目类型ID")
    @Excel(name = "不良项目类型ID", height = 20, width = 30,orderNum="2")
    private Long badItemId;

    /**
     * 不良类型编码
     */
    @Transient
    @ApiModelProperty(name = "badTypeCode",value = "不良类型编码")
    @Excel(name = "不良类型编码", height = 20, width = 30,orderNum="2")
    private String badTypeCode;

    /**
     * 不良类型原因
     */
    @Transient
    @ApiModelProperty(name = "badTypeCause",value = "不良类型原因")
    @Excel(name = "不良类型原因", height = 20, width = 30,orderNum="2")
    private String badTypeCause;

    /**
     * 不良现象编码
     */
    @Transient
    @ApiModelProperty(name = "badPhenomenonCode",value = "不良现象编码")
    @Excel(name = "不良现象编码", height = 20, width = 30,orderNum="2")
    private String badPhenomenonCode;
    
    /**
     * 不良现象名称
     */
    @Transient
    @ApiModelProperty(name = "badPhenomenon",value = "不良现象名称")
    @Excel(name = "不良现象名称", height = 20, width = 30,orderNum="2")
    private String badPhenomenon;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="2")
    private String sectionName;


    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
