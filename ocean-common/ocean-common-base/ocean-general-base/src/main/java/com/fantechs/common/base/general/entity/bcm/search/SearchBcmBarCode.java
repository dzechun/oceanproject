package com.fantechs.common.base.general.entity.bcm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/12/22
 */
@Data
public class SearchBcmBarCode extends BaseQuery implements Serializable {
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 条码类别
     */
    @Transient
    @ApiModelProperty(name="barCodeType" ,value="条码类别(必填)")
    private Integer barCodeType;
    /**
     * 模版名称
     */
    @Transient
    @ApiModelProperty(name="labelName" ,value="模版名称")
    private String labelName;
    /**
     * 打印方式
     */
    @Transient
    @ApiModelProperty(name="printMode" ,value="打印方式")
    private String printMode;
    /**
     * 模版路径
     */
    @Transient
    @ApiModelProperty(name="savePath" ,value="模版路径")
    private String savePath;
}
