package com.fantechs.common.base.electronic.entity.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 标签绑定储位关系历史表
 * smt_ht_electronic_tag_storage
 * @author 53203
 * @date 2020-11-17 14:15:34
 */
@Data
@Table(name = "smt_ht_electronic_tag_storage")
public class SmtHtElectronicTagStorage implements Serializable {
    /**
     * 标签Id绑定储位关系历史Id
     */
    @ApiModelProperty(name="htElectronicTagStorageId",value = "标签Id绑定储位关系历史Id")
    @Excel(name = "标签Id绑定储位关系历史Id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_electronic_tag_storage_id")
    private Long htElectronicTagStorageId;

    /**
     * 标签Id绑定储位关系Id
     */
    @ApiModelProperty(name="electronicTagStorageId",value = "标签Id绑定储位关系Id")
    @Column(name = "electronic_tag_storage_id")
    @NotNull(groups = ValidGroup.update.class,message = "标签Id绑定储位关系Id不能为空")
    private String electronicTagStorageId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Column(name = "storage_id")
    @NotBlank(message = "储位id不能为空")
    private String storageId;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageCode",value = "储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum = "1")
    @Transient
    private String storageCode;

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageName",value = "储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum = "2")
    @Column(name = "storage_name")
    private String storageName;

    /**
     * 储位描述
     */
    @ApiModelProperty(name = "storageDesc",value = "储位描述")
    @Excel(name = "储位描述", height = 20, width = 30,orderNum = "3")
    @Transient
    private String storageDesc;

    /**
     * 设备id（电子标签控制器）
     */
    @ApiModelProperty(name="equipmentId",value = "设备id（电子标签控制器）")
    @Column(name = "equipment_id")
    @NotBlank(message = "设备id（电子标签控制器）")
    private String equipmentId;

    /**
     * 区域设备Id
     */
    @ApiModelProperty(name="equipmentAreaId",value = "区域设备Id")
    @Column(name = "equipment_area_id")
    @NotBlank(message = "区域设备Id不能为空")
    private String equipmentAreaId;

    /**
     * 仓库id
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库名称")
    @Excel(name = "仓库id", height = 20, width = 30,orderNum = "10")
    @Column(name = "warehouse_id")
    @NotBlank(message = "仓库id")
    private String warehouseId;

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
    @Column(name = "warehouse_code")
    private String warehouseCode;

    /**
     * 仓库区域id
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum = "11")
    @Column(name = "warehouse_area_id")
    @NotBlank(message = "仓库区域id不能为空")
    private String warehouseAreaId;

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
    @Column(name = "warehouse_area_code")
    private String warehouseAreaCode;

    /**
     * 物料id
     */
    @ApiModelProperty(name = "materialId",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum = "11")
    @Column(name = "material_id")
    @NotBlank(message = "物料id不能为空")
    private String materialId;

    /**
     * 物料编码
     */
    @Column(name = "material_code")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Column(name = "material_name")
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Transient
    private String materialName;

    /**
     * 物料描述
     */
    @Column(name = "material_desc")
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    @Transient
    private String materialDesc;


    /**
     * 电子标签id
     */
    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    @Excel(name = "电子标签id", height = 20, width = 30,orderNum="7")
    @Column(name = "electronic_tag_id")
    @NotBlank(message = "电子标签id不能为空")
    private String electronicTagId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="9",replace = {"无效_0","有效_1"})
    private Integer status;


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
    @Transient
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
     * 队列名称
     */
    @Transient
    @ApiModelProperty(name="queueName" ,value="队列名称")
    @Excel(name = "队列名称", height = 20, width = 30)
    private String  queueName;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="17",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;


    private static final long serialVersionUID = 1L;
}