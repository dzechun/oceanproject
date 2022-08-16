package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EngReportDeliveryOrderDto implements Serializable {

    @Id
    @ApiModelProperty(name="deliveryOrderDetId",value = "装箱单ID")
    @Column(name = "delivery_order_det_id")
    private String deliveryOrderDetId;

    @ApiModelProperty(name="option2",value = "option2")
    @Column(name = "option2")
    private String option2;  //ISGUID

    @ApiModelProperty(name="option1",value = "option1")
    @Column(name = "option1")
    private String option1;  //IDGUID

    @ApiModelProperty(name="option11",value = "实发量")
    @Column(name = "option11")
    private String option11;

    @ApiModelProperty(name="remark",value = "发料备注")
    @Column(name = "remark")
    private String remark;

    @ApiModelProperty(name="createTime",value = "登记时间")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(name="createUserName",value = "登记人")
    @Column(name = "create_user_name")
    private String createUserName;
}
