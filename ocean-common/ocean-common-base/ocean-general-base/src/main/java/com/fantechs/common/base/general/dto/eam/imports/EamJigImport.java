package com.fantechs.common.base.general.dto.eam.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class EamJigImport implements Serializable {

    /**
     * 治具编码(必填)
     */
    @Excel(name = "治具编码(必填)",  height = 20, width = 30)
    @ApiModelProperty(name="jigCode" ,value="治具编码(必填)")
    private String jigCode;

    /**
     * 治具名称(必填)
     */
    @Excel(name = "治具名称(必填)", height = 20, width = 30)
    @ApiModelProperty(name="jigName" ,value="治具名称(必填)")
    private String jigName;

    /**
     * 治具描述
     */
    @Excel(name = "治具描述", height = 20, width = 30)
    @ApiModelProperty(name="jigDesc" ,value="治具描述")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Excel(name = "治具型号", height = 20, width = 30)
    @ApiModelProperty(name="jigModel" ,value="治具型号")
    private String jigModel;

    /**
     * 治具类别(必填)
     */
    @Excel(name = "治具类别(必填)", height = 20, width = 30)
    @ApiModelProperty(name="jigCategoryCode" ,value="治具类别(必填)")
    private String jigCategoryCode;

    /**
     * 治具类别ID
     */
    @ApiModelProperty(name="jigCategoryId" ,value="治具类别ID")
    private Long jigCategoryId;

    /**
     * 治具管理员编码
     */
    @Excel(name = "治具管理员编码", height = 20, width = 30)
    @ApiModelProperty(name="userCode" ,value="治具管理员编码")
    private String userCode;

    /**
     * 治具管理员ID
     */
    @ApiModelProperty(name="userId" ,value="治具管理员ID")
    private Long userId;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", height = 20, width = 30)
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    private String warehouseCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库ID")
    private Long warehouseId;

    /**
     * 库区编码
     */
    @Excel(name = "库区编码", height = 20, width = 30)
    @ApiModelProperty(name="warehouseAreaCode" ,value="库区编码")
    private String warehouseAreaCode;

    /**
     * 库区ID
     */
    @ApiModelProperty(name="warehouseAreaId" ,value="库区ID")
    private Long warehouseAreaId;

    /**
     * 工作区编码
     */
    @Excel(name = "工作区编码", height = 20, width = 30)
    @ApiModelProperty(name="workingAreaCode" ,value="工作区编码")
    private String workingAreaCode;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId" ,value="工作区ID")
    private Long workingAreaId;

    /**
     * 库位编码
     */
    @Excel(name = "库位编码", height = 20, width = 30)
    @ApiModelProperty(name="storageCode" ,value="库位编码")
    private String storageCode;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId" ,value="库位ID")
    private Long storageId;

    /**
     * 最大使用次数
     */
    @Excel(name = "最大使用次数", height = 20, width = 30)
    @ApiModelProperty(name="maxUsageTime",value = "最大使用次数")
    private Integer maxUsageTime;

    /**
     * 警告次数
     */
    @ApiModelProperty(name="warningTime",value = "警告次数")
    @Excel(name = "警告次数", height = 20, width = 30)
    private Integer warningTime;

    /**
     * 最大使用天数
     */
    @ApiModelProperty(name="maxUsageDays",value = "最大使用天数")
    @Excel(name = "最大使用天数", height = 20, width = 30)
    private Integer maxUsageDays;

    /**
     * 警告天数
     */
    @ApiModelProperty(name="warningDays",value = "警告天数")
    @Excel(name = "警告天数", height = 20, width = 30)
    private Integer warningDays;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    @Excel(name = "长(cm)", height = 20, width = 30)
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Excel(name = "宽(cm)", height = 20, width = 30)
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Excel(name = "高(cm)", height = 20, width = 30)
    private BigDecimal height;

    /**
     * 体积(cm3)
     */
    @ApiModelProperty(name="volume",value = "体积(cm3)")
    @Excel(name = "体积(cm3)", height = 20, width = 30)
    private BigDecimal volume;

    /**
     * 重量(kg)
     */
    @ApiModelProperty(name="weight",value = "重量(kg)")
    @Excel(name = "重量(kg)", height = 20, width = 30)
    private BigDecimal weight;

    /**
     * 电源(kw)
     */
    @ApiModelProperty(name="power",value = "电源(kw)")
    @Excel(name = "电源(kw)", height = 20, width = 30)
    private BigDecimal power;

    /**
     * 保养最大使用次数
     */
    @ApiModelProperty(name="maintainMaxUsageTime",value = "保养最大使用次数")
    @Excel(name = "保养最大使用次数", height = 20, width = 30)
    private Integer maintainMaxUsageTime;

    /**
     * 保养警告次数
     */
    @ApiModelProperty(name="maintainWarningTime",value = "保养警告次数")
    @Excel(name = "保养警告次数", height = 20, width = 30)
    private Integer maintainWarningTime;

    /**
     * 保养周期(天)
     */
    @ApiModelProperty(name="maintainCycle",value = "保养周期(天)")
    @Excel(name = "保养周期(天)", height = 20, width = 30)
    private Integer maintainCycle;

    /**
     * 保养警告天数
     */
    @ApiModelProperty(name="maintainWarningDays",value = "保养警告天数")
    @Excel(name = "保养警告天数", height = 20, width = 30)
    private Integer maintainWarningDays;

    /**
     * 资产编码
     */
    @Excel(name = "资产编码",  height = 20, width = 30)
    @ApiModelProperty(name="assetCode" ,value="资产编码")
    private String assetCode;

    /**
     * 治具条码
     */
    @Excel(name = "治具条码", height = 20, width = 30)
    @ApiModelProperty(name="jigBarcode" ,value="治具条码")
    private String jigBarcode;

    /**
     * 已使用次数
     */
    @Excel(name = "已使用次数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageTime" ,value="已使用次数")
    private Integer currentUsageTime;

    /**
     * 已使用天数
     */
    @Excel(name = "已使用天数", height = 20, width = 30)
    @ApiModelProperty(name="currentUsageDays" ,value="已使用天数")
    private Integer currentUsageDays;

    /**
     * 备用件编码
     */
    @Excel(name = "备用件编码",  height = 20, width = 30)
    @ApiModelProperty(name="sparePartCode" ,value="备用件编码")
    private String sparePartCode;

    /**
     * 备用件ID
     */
    @ApiModelProperty(name="sparePartId" ,value="备用件ID")
    private Long sparePartId;
}
