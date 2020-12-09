package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtSortingList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtSortingListDto extends SmtSortingList implements Serializable {

    /**
     * 标签Id绑定储位关系Id
     */
    @ApiModelProperty(name="electronicTagStorageId",value = "标签Id绑定储位关系Id")
    @Transient
    private Long electronicTagStorageId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Transient
    private Long storageId;


    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "4")
    @Transient
    private String storageName;

    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    @Excel(name = "电子标签id", height = 20, width = 30,orderNum="7")
    @Transient
    private String electronicTagId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId" ,value="物料编码")
    @Transient
    private String materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="8")
    @Transient
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="9")
    @Transient
    private String materialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="10")
    @Transient
    private String materialDesc;

    /**
     * 仓库Id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库Id")
    @Transient
    private String warehouseId;


    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum = "11")
    @Transient
    private String warehouseName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name = "warehouseCode",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum = "12")
    @Transient
    private String warehouseCode;

    /**
     * 仓库区域Id
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域Id")
    @Transient
    private String warehouseAreaId;


    /**
     * 仓库区域名称
     */
    @ApiModelProperty(name = "warehouseAreaName",value = "仓库区域名称")
    @Excel(name = "仓库区域名称", height = 20, width = 30,orderNum = "13")
    @Transient
    private String warehouseAreaName;

    /**
     * 仓库区域编码
     */
    @ApiModelProperty(name = "warehouseAreaCode",value = "仓库区域编码")
    @Excel(name = "仓库区域编码", height = 20, width = 30,orderNum = "14")
    @Transient
    private String warehouseAreaCode;

    /**
     * 设备id（电子标签控制器）
     */
    @ApiModelProperty(name="equipmentId",value = "设备id（电子标签控制器）")
    @Transient
    private Long equipmentId;

    /**
     * 客户端id
     */
    @ApiModelProperty(name="clientId",value = "客户端id")
    @Transient
    private String clientId;

    /**
     * 设备编号
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编号")
    @Excel(name = "设备编号", height = 20, width = 30,orderNum="15")
    @Transient
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    @Excel(name = "设备名称", height = 20, width = 30,orderNum="16")
    @Transient
    private String equipmentName;

    /**
     * 设备数据
     */
    @ApiModelProperty(name="equipmentData",value = "设备数据")
    @Excel(name = "设备数据", height = 20, width = 30,orderNum="17")
    @Transient
    private String equipmentData;

    /**
     * 设备IP地址
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP地址")
    @Excel(name = "设备IP地址", height = 20, width = 30,orderNum="18")
    @Transient
    private String equipmentIp;

    /**
     * 设备端口
     */
    @ApiModelProperty(name="equipmentPort",value = "设备端口")
    @Excel(name = "设备端口", height = 20, width = 30,orderNum="19")
    @Transient
    private String equipmentPort;

    /**
     * 设备类型
     */
    @ApiModelProperty(name="equipmentType",value = "设备类型")
    @Excel(name = "设备类型", height = 20, width = 30,orderNum="20",replace = {"控制器_0", "区域灯_1","其他_2"})
    @Transient
    private Byte equipmentType;

}
