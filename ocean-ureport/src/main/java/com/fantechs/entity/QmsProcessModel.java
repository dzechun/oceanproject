package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author
 * @Date 2021/12/01
 */
@Data
public class QmsProcessModel implements Serializable {

    /**
     * 时间(小时)
     */
    @ApiModelProperty(name="devoteHour",value = "时间(小时)")
    private String devoteHour;

    /**
     * 投入数
     */
    @Transient
    @ApiModelProperty(name="devoteQty" ,value="投入数")
    private int devoteQty;

    /**
     * 不良数
     */
    @Transient
    @ApiModelProperty(name="notGoodQty" ,value="不良数")
    private int notGoodQty;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

}
