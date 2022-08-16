package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigStandingBookDto extends EamJigStandingBook implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="13",needMerge = true)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="15",needMerge = true)
    private String modifiedUserName;

    /**
     * 部门名称
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "部门名称")
    @Excel(name = "部门名称", height = 20, width = 30,orderNum="9",needMerge = true)
    private String deptName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;


    /**
     * 资产条码
     */
    @Transient
    @ApiModelProperty(name = "assetCode",value = "资产条码")
    @Excel(name = "资产条码", height = 20, width = 30,orderNum="1",needMerge = true)
    private String assetCode;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="2",needMerge = true)
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="3",needMerge = true)
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="4",needMerge = true)
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="5",needMerge = true)
    private String jigModel;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="6",needMerge = true)
    private String jigCategoryName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    private String jigBarcode;

    /**
     * 使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)
     */
    @Transient
    @ApiModelProperty(name = "usageStatus",value = "使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)")
    @Excel(name = "使用状态(1-空闲 2-使用中 3-点检中 4-保养中 5-维修中 6-已报废)", height = 20, width = 30,orderNum="11",needMerge = true)
    private Byte usageStatus;

}
