package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2021/1/28
 */
@Data
public class MesPmBreakBulkPrintDto implements Serializable {
    /**
     * 批次号
     */
    @ApiModelProperty(name = "batchNo",value = "批次号")
    @Column(name = "batch_no")
    private String batchNo;
    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;
    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="3")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="10")
    private String routeName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="19")
    private String modifiedUserName;

    /**
     * 产品颜色
     */
    @Transient
    @ApiModelProperty(name = "color",value = "产品颜色")
    private String color;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModuleName",value = "产品型号")
    private String productModuleName;
    /**
     * 产品材质
     */
    @Transient
    @ApiModelProperty(name = "materialQuality",value = "产品材质")
    private String materialQuality;

    @ApiModelProperty(name = "qualityName",value = "抽检员")
    private String qualityName;

    @ApiModelProperty(name = "printDate",value = "打印日期")
    private Date printDate;

    private Long processId;
}
