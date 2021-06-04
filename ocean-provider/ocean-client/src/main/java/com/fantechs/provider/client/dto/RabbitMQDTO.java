package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RabbitMQDTO {

    @ApiModelProperty(name="equipmentTagId",value = "设备标签id（电子标签控制器id）")
    private String equipmentTagId;

    @ApiModelProperty(name="electronicTagId",value = "电子标签id")
    private String electronicTagId;

    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    @ApiModelProperty(name="option1" ,value="灯颜色 默认0 从0-5分别是 红 绿 黄 蓝 紫 浅蓝")
    private String option1;

    @ApiModelProperty(name="option2" ,value="灯频率 默认0 从0-5分别是 熄灭 常亮 2秒1次 1秒1次 0.5秒1次 0.25秒1次")
    private String option2;

    @ApiModelProperty(name="option3" ,value="蜂鸣 默认0 0=关 1=开")
    private String option3;

    @ApiModelProperty(name="queueName" ,value="队列名")
    private String queueName;

    @ApiModelProperty(name="electronicTagLangType",value = "电子标签语言类别(1-中文 2-英文)")
    private Byte electronicTagLangType;
}
