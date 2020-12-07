package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 标签绑定储位关系表
 * smt_electronic_tag_storage
 * @author 53203
 * @date 2020-11-17 14:15:32
 */
@Data
@Table(name = "smt_electronic_tag_storage")
public class SmtElectronicTagStorage extends ValidGroup implements Serializable {
    /**
     * 标签Id绑定储位关系Id
     */
    @ApiModelProperty(name="electronicTagStorageId",value = "标签Id绑定储位关系Id")
    @Id
    @Column(name = "electronic_tag_storage_id")
    @NotNull(groups = update.class,message = "标签Id绑定储位关系Id不能为空")
    private Long electronicTagStorageId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Column(name = "storage_id")
    @NotNull(message = "储位id不能为空")
    private Long storageId;

    /**
     * 设备id（电子标签控制器）
     */
    @ApiModelProperty(name="equipmentId",value = "设备id（电子标签控制器）")
    @Column(name = "equipment_id")
    @NotNull(message = "设备id（电子标签控制器）")
    private Long equipmentId;

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