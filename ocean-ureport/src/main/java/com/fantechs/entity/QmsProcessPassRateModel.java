package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author
 * @Date 2021/12/25
 */
@Data
public class QmsProcessPassRateModel implements Serializable {

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;

    /**
     * 通过率
     */
    @Transient
    @ApiModelProperty(name="passRate" ,value="通过率")
    private String passRate;

    /**
     * 总通过率
     */
    @Transient
    @ApiModelProperty(name="totalPassRate" ,value="总通过率")
    private String totalPassRate;

    /**
     * 预警良率
     */
    @Transient
    @ApiModelProperty(name="alarmRate" ,value="预警良率")
    private String alarmRate;

    /**
     * 停线良率
     */
    @Transient
    @ApiModelProperty(name="stopLineRate" ,value="停线良率")
    private String stopLineRate;

}
