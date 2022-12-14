package com.fantechs.common.base.general.entity.mes.sfc;

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
 * 车间管理-箱码/栈板码补印表
 * mes_sfc_carton_pallet_reprint
 * @author bgkun
 * @date 2021-05-19 17:26:01
 */
@Data
@Table(name = "mes_sfc_carton_pallet_reprint")
public class MesSfcCartonPalletReprint extends ValidGroup implements Serializable {
    /**
     * 箱码/栈板码补印表ID
     */
    @ApiModelProperty(name="cartonPalletReprintId",value = "箱码/栈板码补印表ID")
    @Excel(name = "箱码/栈板码补印表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "carton_pallet_reprint_id")
    private Long cartonPalletReprintId;

    /**
     * 条码类型(1-箱码 2-栈板码)
     */
    @ApiModelProperty(name="barocdeType",value = "条码类型(1-箱码 2-栈板码)")
    @Excel(name = "条码类型(1-箱码 2-栈板码)", height = 20, width = 30,orderNum="") 
    @Column(name = "barocde_type")
    private Byte barocdeType;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 来源条码ID
     */
    @ApiModelProperty(name="sourceBarcodeId",value = "来源条码ID")
    @Excel(name = "来源条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_barcode_id")
    private Long sourceBarcodeId;

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