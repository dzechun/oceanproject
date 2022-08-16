package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生产管理-关键部件关联表
 * mes_sfc_key_part_relevance
 * @author bgkun
 * @date 2021-05-08 20:38:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mes_sfc_key_part_relevance")
public class MesSfcKeyPartRelevance extends ValidGroup implements Serializable {
    /**
     * 关键部件关联表ID
     */
    @ApiModelProperty(name="keyPartRelevanceId",value = "关键部件关联表ID")
    @Excel(name = "关键部件关联表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "key_part_relevance_id")
    private Long keyPartRelevanceId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单编码
     */
    @ApiModelProperty(name="workOrderCode",value = "工单编码")
    @Excel(name = "工单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 产品条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "产品条码ID")
    @Excel(name = "产品条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 产品条码
     */
    @ApiModelProperty(name="barcodeCode",value = "产品条码")
    @Excel(name = "产品条码", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_code")
    private String barcodeCode;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 产线编码
     */
    @ApiModelProperty(name="proCode",value = "产线编码")
    @Excel(name = "产线编码", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_code")
    private String proCode;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_name")
    private String proName;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

    /**
     * 工序编码
     */
    @ApiModelProperty(name="processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30,orderNum="") 
    @Column(name = "process_code")
    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="") 
    @Column(name = "process_name")
    private String processName;

    /**
     * 工位ID
     */
    @ApiModelProperty(name="stationId",value = "工位ID")
    @Excel(name = "工位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 工位编码
     */
    @ApiModelProperty(name="stationCode",value = "工位编码")
    @Excel(name = "工位编码", height = 20, width = 30,orderNum="") 
    @Column(name = "station_code")
    private String stationCode;

    /**
     * 工位名称
     */
    @ApiModelProperty(name="stationName",value = "工位名称")
    @Excel(name = "工位名称", height = 20, width = 30,orderNum="") 
    @Column(name = "station_name")
    private String stationName;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Excel(name = "零件ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "零件编码", height = 20, width = 30,orderNum="")
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "零件名称", height = 20, width = 30,orderNum="")
    @Column(name = "material_name")
    private String materialName;

    /**
     * 产品编码版本
     */
    @ApiModelProperty(name="materialVer",value = "产品编码版本")
    @Excel(name = "零件编码版本", height = 20, width = 30,orderNum="")
    @Column(name = "material_ver")
    private String materialVer;

    /**
     * 标签类别信息ID
     */
    @ApiModelProperty(name="labelCategoryId",value = "标签类别信息ID")
    @Excel(name = "标签类别信息ID", height = 20, width = 30,orderNum="")
    @Column(name = "label_category_id")
    private Long labelCategoryId;

    /**
     * 部件条码
     */
    @ApiModelProperty(name="partBarcode",value = "部件条码")
    @Excel(name = "部件条码", height = 20, width = 30,orderNum="") 
    @Column(name = "part_barcode")
    private String partBarcode;

    /**
     * 部件客户条码
     */
    @ApiModelProperty(name="partCustomerBarcode",value = "部件客户条码")
    @Excel(name = "部件客户条码", height = 20, width = 30,orderNum="") 
    @Column(name = "part_customer_barcode")
    private String partCustomerBarcode;

    /**
     * MAC条码
     */
    @ApiModelProperty(name="mACBarcode",value = "MAC条码")
    @Excel(name = "MAC条码", height = 20, width = 30,orderNum="") 
    @Column(name = "m_a_c_barcode")
    private String mACBarcode;

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