package com.fantechs.common.base.general.entity.ureport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BoxRecordUreport extends ValidGroup implements Serializable {

    /**
     * 箱码
     */
    @ApiModelProperty(name="cartonCode",value = "箱码")
    private String cartonCode;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="nowPackageSpecQty",value = "包装规格")
    private Byte nowPackageSpecQty;

    /**
     * 过站次数
     */
    @ApiModelProperty(name="passStationCount",value = "过站次数")
    private String passStationCount;

    /**
     * 投入时间
     */
    @ApiModelProperty(name="devoteTime",value = "投入时间")
    private Date devoteTime;

    /**
     * 产出时间
     */
    @ApiModelProperty(name="productionTime",value = "产出时间")
    private Date productionTime;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inProcessTime",value = "入站时间")
    private String inProcessTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outProcessTime",value = "出站时间")
    private BigDecimal outProcessTime;

    /**
     * 关箱状态(0-未关闭 1-已关闭)
     */
    @ApiModelProperty(name="closeStatus",value = "关箱状态(0-未关闭 1-已关闭)")
    private Byte closeStatus;

    /**
     * 关箱时间
     */
    @ApiModelProperty(name="closeCartonTime",value = "关箱时间")
    private BigDecimal closeCartonTime;

    /**
     * 关箱人
     */
    @ApiModelProperty(name="userName",value = "关箱人")
    private String userName;
}
