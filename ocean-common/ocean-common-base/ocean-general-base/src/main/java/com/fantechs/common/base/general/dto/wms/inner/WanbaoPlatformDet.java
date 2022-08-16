package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * wanbao_platform_det
 * @author mr.lei
 * @date 2022-06-27 11:41:13
 */
@Data
@Table(name = "wanbao_platform_det")
public class WanbaoPlatformDet extends ValidGroup implements Serializable {
    /**
     * 月台明细id
     */
    @ApiModelProperty(name="platformDetId",value = "月台明细id")
    @Excel(name = "月台明细id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "platform_det_id")
    private Long platformDetId;

    /**
     * 月台id
     */
    @ApiModelProperty(name="platformId",value = "月台id")
    @Excel(name = "月台id", height = 20, width = 30,orderNum="") 
    @Column(name = "platform_id")
    private Long platformId;

    /**
     * 拣货单id
     */
    @ApiModelProperty(name="jobOrderId",value = "拣货单id")
    @Excel(name = "拣货单id", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 拣货单明细id
     */
    @ApiModelProperty(name="jobOrderDetId",value = "拣货单明细id")
    @Excel(name = "拣货单明细id", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 产品id
     */
    @ApiModelProperty(name="materialId",value = "产品id")
    @Excel(name = "产品id", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 成品条码
     */
    @ApiModelProperty(name="barcode",value = "成品条码")
    @Excel(name = "成品条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 客户条码
     */
    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    @Excel(name = "客户条码", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_barcode")
    private String customerBarcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name="salesBarcode",value = "销售条码")
    @Excel(name = "销售条码", height = 20, width = 30,orderNum="") 
    @Column(name = "sales_barcode")
    private String salesBarcode;

    /**
     * 库存明细id
     */
    @ApiModelProperty(name="inventoryDetId",value = "库存明细id")
    @Column(name = "inventory_det_id")
    private Long inventoryDetId;

    /**
     * 库位id
     */
    @ApiModelProperty(name="stroageId",value = "库位id")
    @Column(name = "stroage_id")
    private Long stroageId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}