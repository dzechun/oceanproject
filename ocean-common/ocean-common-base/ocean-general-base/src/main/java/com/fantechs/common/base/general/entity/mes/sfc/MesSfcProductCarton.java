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
 * 生产管理-产品包箱表
 * mes_sfc_product_carton
 * @author bgkun
 * @date 2021-05-08 09:44:47
 */
@Data
@Table(name = "mes_sfc_product_carton")
public class MesSfcProductCarton extends ValidGroup implements Serializable {
    /**
     * 产品包箱表ID
     */
    @ApiModelProperty(name="productCartonId",value = "产品包箱表ID")
    @Excel(name = "产品包箱表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "product_carton_id")
    private Long productCartonId;

    /**
     * 包箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包箱号")
    @Excel(name = "包箱号", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 产品条码
     */
    @ApiModelProperty(name="barcode",value = "产品条码")
    @Excel(name = "产品条码", height = 20, width = 30,orderNum="") 
    private String barcode;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="") 
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="") 
    @Column(name = "material_name")
    private String materialName;

    /**
     * 产品编码版本
     */
    @ApiModelProperty(name="materialVer",value = "产品编码版本")
    @Excel(name = "产品编码版本", height = 20, width = 30,orderNum="") 
    @Column(name = "material_ver")
    private String materialVer;

    /**
     * 条码类别（1.工序流转卡、2.产品条码）
     */
    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.产品条码）")
    @Excel(name = "条码类别（1.工序流转卡、2.产品条码）", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_type")
    private Byte barcodeType;

    /**
     * 作业员ID
     */
    @ApiModelProperty(name="operatorUserId",value = "作业员ID")
    @Excel(name = "作业员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 作业时间
     */
    @ApiModelProperty(name="operatorTime",value = "作业时间")
    @Excel(name = "作业时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "operator_time")
    private Date operatorTime;

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