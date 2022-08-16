package com.fantechs.provider.mes.pm.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderProcessReWo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class MesPmHtWorkOrderProcessReWoVo implements Serializable {

    /**
     * 工单ID
     */
    @Transient
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30)
    private Long workOrderId;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30)
    private String workOrderCode;

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
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 工单集合
     */
    private List<MesPmHtWorkOrderProcessReWo> list;

}
