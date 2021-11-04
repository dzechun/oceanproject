package com.fantechs.common.base.general.entity.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消息内容实体
 */
@Data
public class EngLogisticsRecordMessage implements Serializable {
    /**
     * 材料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    /**
     * 材料名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "材料名称")
    private String materialName;

    /**
     * 合同号
     */
    @Transient
    @ApiModelProperty(name="contractCode",value = "合同号")
    private String contractCode;

    /**
     * 装置号
     */
    @Transient
    @ApiModelProperty(name="deviceCode",value = "装置号")
    private String deviceCode;

    /**
     * 主项号
     */
    @Transient
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    private String dominantTermCode;

    /**
     * 位号
     */
    @Transient
    @ApiModelProperty(name="locationNum",value = "位号")
    private String locationNum;

    /**
     * 单位
     */
    @Transient
    @ApiModelProperty(name="mainUnit",value = "单位")
    private String mainUnit;

    /**
     * 规格
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "规格")
    private String materialDesc;

    /**
     * 相关单号
     */
    @Transient
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    private String relatedOrderCode;

    /**
     * 节点变更时间
     */
    @Transient
    @ApiModelProperty(name="changeTime",value = "节点变更时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date changeTime;

    /**
     * 材料数量
     */
    @Transient
    @ApiModelProperty(name="qty",value = "材料数量")
    private BigDecimal qty;

    /**
     * 操作人
     */
    @Transient
    @ApiModelProperty(name="operateUser",value = "操作人")
    private String operateUser;
}