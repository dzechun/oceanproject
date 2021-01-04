package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SmtElectronicTagStorageDto extends SmtElectronicTagStorage implements Serializable {

    /**
     * 储位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    @Excel(name = "储位描述", height = 20, width = 30,orderNum = "2")
    @Transient
    private String storageDesc;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "3")
    @Transient
    private String storageName;


    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30,orderNum="17")
    @Transient
    private String equipmentName;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码", height = 20, width = 30,orderNum="18")
    @Transient
    private String equipmentCode;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,orderNum="19")
    @Transient
    private String equipmentIp;

    /**
     * 设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "设备端口")
    @Excel(name = "设备端口", height = 20, width = 30,orderNum="20")
    @Transient
    private String equipmentPort;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "6")
    @Transient
    private String warehouseName;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="8")
    @Transient
    private String warehouseAreaName;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="10")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="11")
    @Transient
    private String materialDesc;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="13")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="15")
    private String modifiedUserName;

    /**
     * 队列名称
     */
    @Transient
    @ApiModelProperty(name="queueName" ,value="队列名称")
    @Excel(name = "队列名称", height = 20, width = 30,orderNum="16")
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

}
