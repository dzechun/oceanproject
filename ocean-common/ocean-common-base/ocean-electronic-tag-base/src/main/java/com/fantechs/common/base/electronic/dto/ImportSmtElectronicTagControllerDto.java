package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportSmtElectronicTagControllerDto {

    /**
     * 电子标签控制器编码
     */
    @ApiModelProperty(name="electronicTagControllerCode",value = "电子标签控制器编码")
    @Excel(name = "电子标签控制器编码", height = 20, width = 30,isImportField = "true")
    private String electronicTagControllerCode;

    /**
     * 电子标签控制器名称
     */
    @ApiModelProperty(name="electronicTagControllerName",value = "电子标签控制器名称")
    @Excel(name = "电子标签控制器名称", height = 20, width = 30,isImportField = "true")
    private String electronicTagControllerName;

    /**
     * 电子标签控制器描述
     */
    @ApiModelProperty(name="electronicTagControllerDesc",value = "电子标签控制器描述")
    @Excel(name = "电子标签控制器描述", height = 20, width = 30,isImportField = "true")
    private String electronicTagControllerDesc;

    /**
     * 电子标签控制器ip
     */
    @ApiModelProperty(name="electronicTagControllerIp",value = "电子标签控制器ip")
    @Excel(name = "电子标签控制器ip", height = 20, width = 30,isImportField = "true")
    private String electronicTagControllerIp;

    /**
     * 电子标签控制器端口
     */
    @ApiModelProperty(name="electronicTagControllerPort",value = "电子标签控制器端口")
    @Excel(name = "电子标签控制器端口", height = 20, width = 30,isImportField = "true")
    private String electronicTagControllerPort;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0","有效_1"},isImportField = "true")
    private Integer status;

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,isImportField = "true")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,isImportField = "true")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,isImportField = "true")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,isImportField = "true")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

}
