package com.fantechs.common.base.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtEquipment extends BaseQuery implements Serializable {


    /**
     * 客户端编号
     */
    @ApiModelProperty(name="equipmentCode",value = "客户端编号")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="deviceName",value = "设备名称")
    private String deviceName;

    /**
     * 设备数据
     */
    @ApiModelProperty(name="deviceData",value = "设备数据")
    private String deviceData;

    /**
     * 设备类型
     */
    @ApiModelProperty(name="type",value = "设备类型")
    private Byte type;
}
