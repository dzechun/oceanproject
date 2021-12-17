package com.fantechs.common.base.general.entity.wms.inner;

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
 * 来料条码补打表
 * wms_inner_material_barcode_reprint
 * @author jbb
 * @date 2021-12-16 18:09:38
 */
@Data
@Table(name = "wms_inner_material_barcode_reprint")
public class WmsInnerMaterialBarcodeReprint extends ValidGroup implements Serializable {
    /**
     * 来料条码补打表ID
     */
    @ApiModelProperty(name="materialBarcodeReprintId",value = "来料条码补打表ID")
    @Excel(name = "来料条码补打表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "material_barcode_reprint_id")
    private Long materialBarcodeReprintId;

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    @Excel(name = "来料条码ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_barcode_id")
    private Long materialBarcodeId;

    /**
     * 打印指来源端IP
     */
    @ApiModelProperty(name="printOrderSourceIp",value = "打印指来源端IP")
    @Excel(name = "打印指来源端IP", height = 20, width = 30,orderNum="")
    @Column(name = "print_order_source_ip")
    private String printOrderSourceIp;

    /**
     * 打印机名称
     */
    @ApiModelProperty(name="printerName",value = "打印机名称")
    @Excel(name = "打印机名称", height = 20, width = 30,orderNum="")
    @Column(name = "printer_name")
    private String printerName;

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
