package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 作业单
 * wms_inner_barcode_operation
 * @author Dylan
 * @date 2022-03-09 11:00:55
 */
@Data
@Table(name = "wms_inner_barcode_operation")
public class WmsInnerBarcodeOperation extends ValidGroup implements Serializable {
    /**
     * 条码操作表ID
     */
    @ApiModelProperty(name="barcodeOperationId",value = "条码操作表ID")
    @Id
    @Column(name = "barcode_operation_id")
    private Long barcodeOperationId;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30)
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30)
    private String barcode;

    /**
     * 替换条码
     */
    @ApiModelProperty(name="replaceBarcode",value = "替换条码")
    @Excel(name = "替换条码", height = 20, width = 30)
    @Column(name = "replace_barcode")
    private String replaceBarcode;

    /**
     * 出货口
     */
    @ApiModelProperty(name="outPort",value = "出货口")
    @Excel(name = "出货口", height = 20, width = 30)
    @Column(name = "out_port")
    private String outPort;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="operationType",value = "单据类型")
    @Excel(name = "单据类型", height = 20, width = 30)
    @Column(name = "operation_type")
    private String operationType;

    /**
     * 来源单ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "来源单ID")
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}