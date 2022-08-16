package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJigBarcode;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EamJigBarcodeDto extends EamJigBarcode implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
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
    @ApiModelProperty(name="jigCode",value = "治具编码")
    @Transient
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    @Transient
    private String jigName;

    /**
     * 治具描述
     */
    @ApiModelProperty(name="jigDesc",value = "治具描述")
    @Transient
    private String jigDesc;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    @Transient
    private String jigModel;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    private String jigCategoryName;

    /**
     * 治具类别ID
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryId",value = "治具类别ID")
    private Long jigCategoryId;

    /**
     * 治具管理员
     */
    @Transient
    @ApiModelProperty(name = "userName",value = "治具管理员")
    private String userName;
}
