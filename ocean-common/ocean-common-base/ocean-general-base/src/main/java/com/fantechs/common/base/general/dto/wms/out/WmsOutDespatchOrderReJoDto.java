package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Data
public class WmsOutDespatchOrderReJoDto extends WmsOutDespatchOrderReJo implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "作业单号")
    @Excel(name = "作业单号", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_code")
    private String jobOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 数量
     */
    @ApiModelProperty(name="actualQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="")
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    @Transient
    @ApiModelProperty(name = "orderStatus",value = "单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成)")
    private Byte orderStatus;

    /**
     * 收货人名称
     */
    @Transient
    @ApiModelProperty(name="consigneeName",value = "发货人名称")
    @Excel(name = "发货人名称", height = 20, width = 30,orderNum="")
    private String consigneeName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="")
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30,orderNum="")
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @Transient
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30,orderNum="")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @Transient
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="")
    private String eMailAddress;

    /**
     * 详细地址
     */
    @Transient
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="")
    private String detailedAddress;
}
