package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 静电地线检测记录
 * @author admin
 */
@Data
public class QmsIpqcDtaticElectricityModel extends ValidGroup implements Serializable {

    /**
     * IPQC检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单ID")
    private Long ipqcInspectionOrderId;

    /**
     * IPQC检验单编码
     */
    @ApiModelProperty(name="ipqcInspectionOrderCode",value = "检验单号")
    @Excel(name = "检验单号", height = 20, width = 30,orderNum="1")
    private String ipqcInspectionOrderCode;

    /**
     * 检验项目
     */
    @ApiModelProperty(name="inspectionWayDesc",value = "检验项目")
    @Excel(name = "检验项目", height = 20, width = 30,orderNum="2")
    private String inspectionWayDesc;

    /**
     * 检验结果(1-合格 2-不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(1-合格 2-不合格)")
    @Excel(name = "检验结果(1-合格 2-不合格)", height = 20, width = 30,orderNum="3")
    private Byte inspectionResult;

    /**
     * 检验时间
     */
    @ApiModelProperty(name="modifiedTime",value = "检验时间 yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检验时间", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    /**
     * 测试人
     */
    @ApiModelProperty(name="modifiedUserName",value = "测试人")
    @Excel(name = "测试人", height = 20, width = 30,orderNum="5")
    private String modifiedUserName;




}