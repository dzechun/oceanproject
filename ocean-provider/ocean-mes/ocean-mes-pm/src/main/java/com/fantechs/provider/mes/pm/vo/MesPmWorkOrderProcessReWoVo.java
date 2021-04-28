package com.fantechs.provider.mes.pm.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

public class MesPmWorkOrderProcessReWoVo implements Serializable {

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="orderCode",value = "订单号")
    @Excel(name = "订单号", height = 20, width = 30)
    private String orderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 产品料号描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品料号描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name="version",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30)
    private String version;

    /**
     * 工单集合
     */
    private List<MesPmWorkOrderProcessReWoDto> list;

}
