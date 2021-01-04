package com.fantechs.common.base.electronic.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.electronic.entity.SmtEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class SmtEquipmentDto extends SmtEquipment implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

//    /**
//     * 控制器设备对应的电子储位列表
//     */
//    @Transient
//    @ApiModelProperty(name = "electronicTagStorageList",value = "储位集合")
//    private List<SmtElectronicTagStorageDto> electronicTagStorageList;

    /**
     * 队列名称
     */
    @Transient
    @ApiModelProperty(name="queueName" ,value="队列名称")
    @Excel(name = "队列名称", height = 20, width = 30)
    private String  queueName;

    /**
     * 客户端名称
     */
    @Transient
    @ApiModelProperty(name="clientName" ,value="客户端名称")
    @Excel(name = "客户端名称", height = 20, width = 30)
    private String  clientName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

}
