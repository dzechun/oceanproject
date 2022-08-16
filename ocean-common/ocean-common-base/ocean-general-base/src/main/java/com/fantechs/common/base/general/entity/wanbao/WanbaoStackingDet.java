package com.fantechs.common.base.general.entity.wanbao;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 堆码号明细
 * wanbao_stacking_det
 * @author bgkun
 * @date 2022-01-21 10:04:57
 */
@Data
@Table(name = "wanbao_stacking_det")
public class WanbaoStackingDet extends ValidGroup implements Serializable {
    /**
     * 堆码号明细ID
     */
    @ApiModelProperty(name="stackingDetId",value = "堆码号明细ID")
    @Excel(name = "堆码号明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stacking_det_id")
    private Long stackingDetId;

    /**
     * 堆码号ID
     */
    @ApiModelProperty(name="stackingId",value = "堆码号ID")
    @Excel(name = "堆码号ID", height = 20, width = 30,orderNum="") 
    @Column(name = "stacking_id")
    private Long stackingId;

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