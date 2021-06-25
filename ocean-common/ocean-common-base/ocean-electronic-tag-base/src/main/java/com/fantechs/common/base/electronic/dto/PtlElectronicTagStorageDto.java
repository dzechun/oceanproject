package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.PtlElectronicTagStorage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PtlElectronicTagStorageDto extends PtlElectronicTagStorage implements Serializable {

    /**
     * 单据类型（1-分拣单 2-上料单）
     */
    @ApiModelProperty(name="orderType",value = "单据类型（1-分拣单 2-上料单）")
    private Byte orderType;

    /**
     * 储位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    private String storageDesc;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "2")
    @Transient
    private String storageName;

    /**
     * 设备标签ID
     */
    @ApiModelProperty(name="equipmentTagId",value = "设备标签ID")
    private String equipmentTagId;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30,orderNum="4")
    @Transient
    private String equipmentName;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码(必填)", height = 20, width = 30,orderNum="3")
    @Transient
    private String equipmentCode;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,orderNum="7")
    @Transient
    private String equipmentIp;

    /**
     * 设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "设备端口")
    @Excel(name = "设备端口", height = 20, width = 30,orderNum="8")
    @Transient
    private String equipmentPort;

    /**
     * 区域设备标签ID
     */
    @ApiModelProperty(name="equipmentAreaTagId",value = "区域设备标签ID")
    private String equipmentAreaTagId;

    /**
     * 区域设备标签ID顺序位置
     */
    @ApiModelProperty(name="position",value = "区域设备标签ID顺序位置")
    private String position;

    /**
     * 区域设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "区域设备名称")
    @Excel(name = "区域设备名称", height = 20, width = 30,orderNum="6")
    @Transient
    private String equipmentAreaName;

    /**
     * 区域设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "区域设备编码")
    @Excel(name = "区域设备编码(必填)", height = 20, width = 30,orderNum="5")
    @Transient
    private String equipmentAreaCode;

    /**
     * 区域设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "区域设备ip")
    private String equipmentAreaIp;

    /**
     * 区域设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "区域设备端口")
    private String equipmentAreaPort;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "12")
    @Transient
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="14")
    @Transient
    private String warehouseAreaName;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion" ,value="物料版本")
    private String materialVersion;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="16")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="18")
    private String modifiedUserName;

    @ApiModelProperty(name="clientId",value = "客户端id")
    private Long clientId;

    /**
     * 队列名称
     */
    @Transient
    @ApiModelProperty(name="queueName" ,value="队列名称")
    private String  queueName;

    /**
     * 数量
     */
    @ApiModelProperty(name="quantity" ,value="数量")
    private BigDecimal quantity;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 实际数量
     */
    @ApiModelProperty(name="actualQty",value = "实际数量")
    private BigDecimal actualQty;


}
