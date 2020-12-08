package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SmtElectronicTagStorageDto extends SmtElectronicTagStorage implements Serializable {

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "1")
    @Transient
    private String storageName;

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum = "2")
    private String storageCode;

    /**
     * 储位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    @Excel(name = "储位描述", height = 20, width = 30,orderNum = "3")
    @Transient
    private String storageDesc;


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
    @Excel(name = "电子标签控制器编码", height = 20, width = 30,orderNum="5")
    private String equipmentCode;

    /**
     * 设备ip
     */
    @ApiModelProperty(name="equipmentIp",value = "设备ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,orderNum="6")
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
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "10")
    @Transient
    private String warehouseName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum = "11")
    @Transient
    private String warehouseCode;

    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name="warehouseAreaName" ,value="仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum="12")
    @Transient
    private String warehouseAreaName;

    /**
     * 仓库区域编码
     */
    @ApiModelProperty(name="warehouseAreaCode" ,value="仓库区域编码")
    @Excel(name = "仓库区域编码", height = 20, width = 30,orderNum="13")
    private String warehouseAreaCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="14")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="16")
    private String modifiedUserName;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    @NotBlank(message = "物料编码不能为空")
    private String materialCode;

    /**
     * 物料描述
     */
    @Column(name = "material_desc")
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;


    /**
     * 数量
     */
    @Column(name = "quantity")
    @ApiModelProperty(name="quantity" ,value="物料数量")
    @Excel(name = "物料数量", height = 20, width = 30)
    private BigDecimal  quantity;

    /**
     * 队列名称
     */
    @Column(name = "queue_name")
    @ApiModelProperty(name="queueName" ,value="队列名称")
    @Excel(name = "队列名称", height = 20, width = 30)
    private String  queueName;


}
