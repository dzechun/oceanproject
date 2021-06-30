package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 抽检日报表
 * @author admin
 */
@Data
public class QmsIpqcSamplingModel extends ValidGroup implements Serializable {

    /**
     * IPQC检验单编码
     */
    @ApiModelProperty(name="ipqcInspectionOrderCode",value = "检验单号")
    @Excel(name = "检验单号", height = 20, width = 30,orderNum="1")
    private String ipqcInspectionOrderCode;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    /**
     * 流水号
     */
    @ApiModelProperty(name="barcodes",value = "流水号")
    @Excel(name = "流水号", height = 20, width = 30,orderNum="3")
    private String barcodes;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="4")
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="5")
    private String materialDesc;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="6")
    private String materialVersion;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="7")
    private String productModelName;

    /**
     * 数量
     */

    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="8")
    private BigDecimal qty;

    /**
     * 检验结果(1-合格 2-不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(1-合格 2-不合格)")
    @Excel(name = "检验结果(1-合格 2-不合格)", height = 20, width = 30,orderNum="9")
    private Byte inspectionResult;

    /**
     * 责任人
     */
    @ApiModelProperty(name="modifiedUserName",value = "责任人")
    @Excel(name = "责任人", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 检验日期
     */
    @ApiModelProperty(name="modifiedTime",value = "检验时间 yyyy-MM-dd")
    @Excel(name = "检验时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    private Date modifiedTime;

    /**
     * IPQC检验单明细
     */
    @Transient
    private List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = new ArrayList<>();

}