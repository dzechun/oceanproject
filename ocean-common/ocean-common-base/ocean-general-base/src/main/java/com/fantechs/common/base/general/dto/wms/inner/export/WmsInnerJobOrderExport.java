package com.fantechs.common.base.general.dto.wms.inner.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class WmsInnerJobOrderExport implements Serializable {

    /**
     * 作业单号
     */
    @Excel(name = "作业单号",  height = 20, width = 30)
    private String jobOrderCode;

    /**
     * 来源单据类型
     */
    @Excel(name = "来源单据类型",  height = 20, width = 30,replace = {"OUT-SO_销售订单","MES-WO_生产订单","IN-PO_采购订单","OUT-OOO_其他出库单"})
    private String sourceSysOrderTypeCode;

    /**
     * 仓库
     */
    @Excel(name = "仓库",  height = 20, width = 30)
    private String warehouseName;

    /**
     * 工作人员
     */
    @Excel(name = "工作人员",  height = 20, width = 30)
    private String workName;

    /**
     * 单据作业开始时间
     */
    @Excel(name = "单据作业开始时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private Date orderWorkStartTime;

    /**
     * 单据作业结束时间
     */
    @Excel(name = "单据作业结束时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private Date orderWorkEndtTime;

    /**
     * 总计划数量
     */
    @Excel(name = "总计划数量",  height = 20, width = 30)
    private BigDecimal totalPlanQty;

    /**
     * 总分配数量
     */
    @Excel(name = "总分配数量",  height = 20, width = 30)
    private BigDecimal totalDistributionQty;

    /**
     * 总拣货数量
     */
    @Excel(name = "总拣货数量",  height = 20, width = 30)
    private BigDecimal totalActualQty;

    /**
     * 单据状态
     */
    @Excel(name = "单据状态",  height = 20, width = 30,replace = {"1_待分配","2_分配中","3_待作业","4_作业中","5_完成"})
    private String orderStatus;

    /**
     * 备注
     */
    @Excel(name = "备注",  height = 20, width = 30)
    private String remark;

    /**
     * 创建用户
     */
    @Excel(name = "创建用户",  height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 修改用户
     */
    @Excel(name = "修改用户",  height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Excel(name = "修改时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private String modifiedTime;

    /**
     * 来源单号
     */
    @Excel(name = "来源单号",  height = 20, width = 30)
    private String sourceOrderCode;

    /**
     * 移入库位
     */
    @Excel(name = "移入库位",  height = 20, width = 30)
    private String inStorageCode;

    /**
     * 移出库位
     */
    @Excel(name = "移出库位",  height = 20, width = 30)
    private String outStorageCode;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",  height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",  height = 20, width = 30)
    private String materialName;

    /**
     * 物料版本
     */
    @Excel(name = "物料版本",  height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @Excel(name = "物料描述",  height = 20, width = 30)
    private String materialDesc;

    /**
     * 单位
     */
    @Excel(name = "单位",  height = 20, width = 30)
    private String unit;

    /**
     * 计划数量
     */
    @Excel(name = "计划数量",  height = 20, width = 30)
    private BigDecimal planQty;

    /**
     * 分配数量
     */
    @Excel(name = "分配数量",  height = 20, width = 30)
    private BigDecimal distributionQty;

    /**
     * 拣货数量
     */
    @Excel(name = "拣货数量",  height = 20, width = 30)
    private BigDecimal actualQty;

    /**
     * 作业开始时间
     */
    @Excel(name = "作业开始时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private Date workStartTime;

    /**
     * 作业结束时间
     */
    @Excel(name = "作业结束时间",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private String workEndTime;

    /**
     * 行状态
     */
    @Excel(name = "行状态",  height = 20, width = 30,replace = {"1_待分配","2_待作业","3_完成"})
    private String lineStatus;

    /**
     * SN码
     */
    @Excel(name = "SN码",  height = 20, width = 30)
    private String barcode;

    /**
     * 彩盒码
     */
    @Excel(name = "彩盒码",  height = 20, width = 30)
    private String cartonCode;

    /**
     * 箱码
     */
    @Excel(name = "箱码",  height = 20, width = 30)
    private String colorBoxCode;

    /**
     * 栈板码
     */
    @Excel(name = "栈板码",  height = 20, width = 30)
    private String palletCode;

    /**
     * 非系统条码
     */
    @Excel(name = "非系统条码",  height = 20, width = 30,replace = {"1_是","0_否"})
    private String ifSysBarcode;

    /**
     * 批号
     */
    @Excel(name = "批号",  height = 20, width = 30)
    private String batchCode;

    /**
     * 生产日期
     */
    @Excel(name = "生产日期",  height = 20, width = 30,format = "YYYY-MM-dd HH:mm:ss")
    private Date productionTime;

    /**
     * 数量
     */
    @Excel(name = "数量",  height = 20, width = 30)
    private BigDecimal materialQty;

}
