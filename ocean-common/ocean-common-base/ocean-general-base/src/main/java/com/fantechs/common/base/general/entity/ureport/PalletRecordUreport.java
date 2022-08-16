package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PalletRecordUreport extends ValidGroup implements Serializable {

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum = "1")
    private String palletCode;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="nowPackageSpecQty",value = "包装规格")
    @Excel(name = "包装规格", height = 20, width = 30,orderNum = "2")
    private Byte nowPackageSpecQty;

    /**
     * 过站次数
     */
    @ApiModelProperty(name="passStationCount",value = "过站次数")
    @Excel(name = "过站次数", height = 20, width = 30,orderNum = "3")
    private String passStationCount;

    /**
     * 投入时间
     */
    @ApiModelProperty(name="devoteTime",value = "投入时间")
    @Excel(name = "投入时间", height = 20, width = 30,orderNum = "4",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date devoteTime;

    /**
     * 产出时间
     */
    @ApiModelProperty(name="productionTime",value = "产出时间")
    @Excel(name = "产出时间", height = 20, width = 30,orderNum = "5",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date productionTime;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inProcessTime",value = "入站时间")
    @Excel(name = "入站时间", height = 20, width = 30,orderNum = "6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date inProcessTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outProcessTime",value = "出站时间")
    @Excel(name = "出站时间", height = 20, width = 30,orderNum = "7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date outProcessTime;

    /**
     * 关栈板状态(0-未关闭 1-已关闭)
     */
    @ApiModelProperty(name="closeStatus",value = "关箱状态(0-未关闭 1-已关闭)")
    @Excel(name = "关栈板状态", height = 20, width = 30,orderNum = "8",replace = {"未关闭_0","已关闭_1"})
    private Byte closeStatus;

    /**
     * 关栈板时间
     */
    @ApiModelProperty(name="closeCartonTime",value = "关箱时间")
    @Excel(name = "关栈板时间", height = 20, width = 30,orderNum = "9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    private Date closeCartonTime;

    /**
     * 关栈板人
     */
    @ApiModelProperty(name="userName",value = "关箱人")
    @Excel(name = "关栈板人", height = 20, width = 30,orderNum = "10")
    private String userName;

}
