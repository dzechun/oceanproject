package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
public class EamJigRequisitionDto extends EamJigRequisition implements Serializable {

    /**
     * 使用用户名称
     */
    @Transient
    @ApiModelProperty(name = "usageUserName",value = "使用用户名称")
    @Excel(name = "使用用户名称", height = 20, width = 30,orderNum="16")
    private String usageUserName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="17")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="19")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="3")
    private String workOrderCode;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="2")
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
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="7")
    private String jigCategoryName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="1")
    private String jigBarcode;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    @Excel(name = "仓库", height = 20, width = 30,orderNum="8")
    private String warehouseName;

    /**
     * 库区
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区")
    @Excel(name = "库区", height = 20, width = 30,orderNum="9")
    private String warehouseAreaName;

    /**
     * 工作区
     */
    @Transient
    @ApiModelProperty(name = "workingAreaCode",value = "工作区")
    @Excel(name = "工作区", height = 20, width = 30,orderNum="10")
    private String workingAreaCode;

    /**
     * 库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "库位")
    @Excel(name = "库位", height = 20, width = 30,orderNum="11")
    private String storageCode;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="20")
    private String equipmentName;

    /**
     * 领用状态（0-已归还 1-已领用）
     */
    @Transient
    @ApiModelProperty(name = "requisitionStatus",value = "领用状态（0-已归还 1-已领用）")
    @Excel(name = "领用状态（0-已归还 1-已领用）", height = 20, width = 30,orderNum="12")
    private Byte requisitionStatus;

    /**
     * 领用时间
     */
    @Transient
    @ApiModelProperty(name = "requisitionTime",value = "领用时间")
    @Excel(name = "领用时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date requisitionTime;

    /**
     * 归还时间
     */
    @Transient
    @ApiModelProperty(name = "returnTime",value = "归还时间")
    @Excel(name = "归还时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

}
