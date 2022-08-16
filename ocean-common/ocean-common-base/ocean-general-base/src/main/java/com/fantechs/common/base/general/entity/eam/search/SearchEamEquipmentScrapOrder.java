package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentScrapOrder extends BaseQuery implements Serializable {

    /**
     * 报废单编码
     */
    @ApiModelProperty(name="equipmentScrapOrderCode",value = "报废单编码")
    private String equipmentScrapOrderCode;

    /**
     * 申请部门
     */
    @ApiModelProperty(name="applyDeptName",value = "申请部门")
    private String applyDeptName;

    /**
     * 使用人成本中心
     */
    @ApiModelProperty(name="useDeptName",value = "使用人成本中心")
    private String useDeptName;

    /**
     * 申请人
     */
    @ApiModelProperty(name="applyUserName",value = "申请人")
    private String applyUserName;

    /**
     * 使用人姓名
     */
    @ApiModelProperty(name="useUserName",value = "使用人姓名")
    private String useUserName;

    /**
     * 单据状态
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态")
    private Byte orderStatus;

    /**
     * 申请时间(YYYY-MM-DD)
     */
    @ApiModelProperty(name="applyTime",value = "申请时间(YYYY-MM-DD)")
    private String applyTime;

}
