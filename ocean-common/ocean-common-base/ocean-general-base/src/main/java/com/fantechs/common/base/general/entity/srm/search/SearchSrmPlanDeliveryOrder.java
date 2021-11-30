package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchSrmPlanDeliveryOrder extends BaseQuery implements Serializable {

    /**
     * 送货计划单编码
     */
    @ApiModelProperty(name="planDeliveryOrderCode",value = "送货计划单编码")
    private String planDeliveryOrderCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 单据状态(1-未提交 2-提交)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-未提交 2-提交)")
    private Byte orderStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改时间开始
     */
    @ApiModelProperty(name="modifiedTimeStart",value = "修改时间开始")
    private String modifiedTimeStart;

    /**
     * 修改时间结束
     */
    @ApiModelProperty(name="modifiedTimeEnd",value = "修改时间结束")
    private String modifiedTimeEnd;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;


    private static final long serialVersionUID = 1L;
}
