package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchWorkOrderFinished extends BaseQuery implements Serializable {

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractNo",value = "合同号")
    private String contractNo;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 线别代码
     */
    @ApiModelProperty(name="proCode" ,value="线别代码")
    private String proCode;

    /**
     * 工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)
     */
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态(0、待生产 1、待首检 2、生产中 3、暂停生产 4、生产完成 5、工单挂起)")
    private Integer workOrderStatus;

    /**
     * 操作时间
     */
    @ApiModelProperty(name="createTime" ,value="操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
