package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 生产管理-生产订单部件条码补印表
 * mes_sfc_work_order_part_barcode_reprint
 * @author bgkun
 * @date 2021-06-17 14:23:37
 */
@Data
@Table(name = "mes_sfc_work_order_part_barcode_reprint")
public class MesSfcWorkOrderPartBarcodeReprint extends ValidGroup implements Serializable {
    /**
     * 生产订单部件条码补印表ID
     */
    @ApiModelProperty(name="workOrderPartBarcodeReprintId",value = "生产订单部件条码补印表ID")
    @Excel(name = "生产订单部件条码补印表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "work_order_part_barcode_reprint_id")
    private Long workOrderPartBarcodeReprintId;

    /**
     * 生产订单部件条码ID
     */
    @ApiModelProperty(name="workOrderPartBarcodeId",value = "生产订单部件条码ID")
    @Excel(name = "生产订单部件条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_part_barcode_id")
    private Long workOrderPartBarcodeId;

    /**
     * 部件条码
     */
    @ApiModelProperty(name="partBarcodeCode",value = "部件条码")
    @Excel(name = "部件条码", height = 20, width = 30,orderNum="") 
    @Column(name = "part_barcode_code")
    private String partBarcodeCode;

    /**
     * 标签类别ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别ID")
    @Excel(name = "标签类别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 确认用户ID
     */
    @ApiModelProperty(name="comfirmUserId",value = "确认用户ID")
    @Excel(name = "确认用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "comfirm_user_id")
    private Long comfirmUserId;

    /**
     * 确认时间
     */
    @ApiModelProperty(name="comfirmTime",value = "确认时间")
    @Excel(name = "确认时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "comfirm_time")
    private Date comfirmTime;

    /**
     * 补印用户ID
     */
    @ApiModelProperty(name="reprintUserId",value = "补印用户ID")
    @Excel(name = "补印用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "reprint_user_id")
    private Long reprintUserId;

    /**
     * 补印时间
     */
    @ApiModelProperty(name="reprintTime",value = "补印时间")
    @Excel(name = "补印时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "reprint_time")
    private Date reprintTime;

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