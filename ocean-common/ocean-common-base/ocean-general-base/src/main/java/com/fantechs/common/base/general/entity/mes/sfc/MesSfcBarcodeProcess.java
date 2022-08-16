package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 生产管理-产品条码过站表
 * mes_sfc_barcode_process
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mes_sfc_barcode_process")
public class MesSfcBarcodeProcess extends ValidGroup implements Serializable {
    /**
     * 条码过站表ID
     */
    @ApiModelProperty(name="barcodeProcessId",value = "条码过站表ID")
    @Excel(name = "条码过站表ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "barcode_process_id")
    private Long barcodeProcessId;

    /**
     * 工单条码表ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "工单条码表ID")
    @Excel(name = "工单条码表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 产品条码
     */
    @ApiModelProperty(name="barcode",value = "产品条码")
    @Excel(name = "产品条码", height = 20, width = 30,orderNum="") 
    private String barcode;

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
     * 条码类别（1.工序流转卡、2.工单条码、3.客户条码）
     */
    @ApiModelProperty(name="barcodeType",value = "条码类别（1.工序流转卡、2.工单条码、3.客户条码）")
    @Excel(name = "条码类别（1.工序流转卡、2.工单条码、3.客户条码）", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_type")
    private Byte barcodeType;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialCode",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="") 
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 产品编码版本
     */
    @ApiModelProperty(name="materialVer",value = "产品编码版本")
    @Excel(name = "产品编码版本", height = 20, width = 30,orderNum="") 
    @Column(name = "material_ver")
    private String materialVer;

    /**
     * 产品编码名称
     */
    @ApiModelProperty(name="materialName",value = "产品编码名称")
    @Excel(name = "产品编码名称", height = 20, width = 30,orderNum="") 
    @Column(name = "material_name")
    private String materialName;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(name="routeId",value = "工艺路线ID")
    @Excel(name = "工艺路线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(name="routeCode",value = "工艺路线编码")
    @Excel(name = "工艺路线编码", height = 20, width = 30,orderNum="") 
    @Column(name = "route_code")
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName",value = "工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="") 
    @Column(name = "route_name")
    private String routeName;

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
     * 工段ID
     */
    @ApiModelProperty(name="sectionId",value = "工段ID")
    @Excel(name = "工段ID", height = 20, width = 30,orderNum="") 
    @Column(name = "section_id")
    private Long sectionId;

    /**
     * 工段编码
     */
    @ApiModelProperty(name="sectionCode",value = "工段编码")
    @Excel(name = "工段编码", height = 20, width = 30,orderNum="") 
    @Column(name = "section_code")
    private String sectionCode;

    /**
     * 工段名称
     */
    @ApiModelProperty(name="sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30,orderNum="") 
    @Column(name = "section_name")
    private String sectionName;

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
     * 下一工序ID
     */
    @ApiModelProperty(name="nextProcessId",value = "下一工序ID")
    @Excel(name = "下一工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "next_process_id")
    private Long nextProcessId;

    /**
     * 下一工序编码
     */
    @ApiModelProperty(name="nextProcessCode",value = "下一工序编码")
    @Excel(name = "下一工序编码", height = 20, width = 30,orderNum="") 
    @Column(name = "next_process_code")
    private String nextProcessCode;

    /**
     * 下一工序名称
     */
    @ApiModelProperty(name="nextProcessName",value = "下一工序名称")
    @Excel(name = "下一工序名称", height = 20, width = 30,orderNum="") 
    @Column(name = "next_process_name")
    private String nextProcessName;

    /**
     * 产品条码状态(0-NG 1-OK)
     */
    @ApiModelProperty(name="barcodeStatus",value = "产品条码状态(0-NG 1-OK)")
    @Excel(name = "产品条码状态(0-NG 1-OK)", height = 20, width = 30,orderNum="") 
    @Column(name = "barcode_status")
    private Byte barcodeStatus;

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionCode",value = "检验单号")
    @Excel(name = "检验单号", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_code")
    private String inspectionCode;

    /**
     * 检验结果
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果")
    @Excel(name = "检验结果", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_result")
    private String inspectionResult;

    /**
     * 客户料号ID
     */
    @ApiModelProperty(name="customerMaterialId",value = "客户料号ID")
    @Excel(name = "客户料号ID", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_material_id")
    private Long customerMaterialId;

    /**
     * 客户料号编码
     */
    @ApiModelProperty(name="customerMaterialCode",value = "客户料号编码")
    @Excel(name = "客户料号编码", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_material_code")
    private String customerMaterialCode;

    /**
     * 客户料号名称
     */
    @ApiModelProperty(name="customerMaterialName",value = "客户料号名称")
    @Excel(name = "客户料号名称", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_material_name")
    private String customerMaterialName;

    /**
     * 客户条码ID
     */
    @ApiModelProperty(name="customerBarcodeId",value = "客户条码ID")
    @Excel(name = "客户条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_barcode_id")
    private Long customerBarcodeId;

    /**
     * 客户条码
     */
    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    @Excel(name = "客户条码", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_barcode")
    private String customerBarcode;

    /**
     * 是否挂起（0-否 1-是）
     */
    @ApiModelProperty(name="ifHangUp",value = "是否挂起（0-否 1-是）")
    @Excel(name = "是否挂起（0-否 1-是）", height = 20, width = 30,orderNum="") 
    @Column(name = "if_hang_up")
    private Byte ifHangUp;

    /**
     * 合板编码
     */
    @ApiModelProperty(name="mergePalletCode",value = "合板编码")
    @Excel(name = "合板编码", height = 20, width = 30,orderNum="") 
    @Column(name = "merge_pallet_code")
    private String mergePalletCode;

    /**
     * ECN编码
     */
    @ApiModelProperty(name="eCNCode",value = "ECN编码")
    @Excel(name = "ECN编码", height = 20, width = 30,orderNum="") 
    @Column(name = "e_c_n_code")
    private String eCNCode;

    /**
     * 返工编号
     */
    @ApiModelProperty(name="reworkCode",value = "返工编号")
    @Excel(name = "返工编号", height = 20, width = 30,orderNum="") 
    @Column(name = "rework_code")
    private String reworkCode;

    /**
     * 返工单ID
     */
    @ApiModelProperty(name="reworkOrderId",value = "返工单ID")
    @Excel(name = "返工单ID", height = 20, width = 30,orderNum="")
    @Column(name = "rework_order_id")
    private Long reworkOrderId;


    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30,orderNum="") 
    @Column(name = "color_box_code")
    private String colorBoxCode;

    /**
     * 包箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包箱号")
    @Excel(name = "包箱号", height = 20, width = 30,orderNum="") 
    @Column(name = "carton_code")
    private String cartonCode;

    /**
     * 过站次数
     */
    @ApiModelProperty(name="passStationCount",value = "过站次数")
    @Excel(name = "过站次数", height = 20, width = 30,orderNum="") 
    @Column(name = "pass_station_count")
    private Integer passStationCount;

    /**
     * 投入时间
     */
    @ApiModelProperty(name="devoteTime",value = "投入时间")
    @Excel(name = "投入时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "devote_time")
    private Date devoteTime;

    /**
     * 产出时间
     */
    @ApiModelProperty(name="productionTime",value = "产出时间")
    @Excel(name = "产出时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 入站时间
     */
    @ApiModelProperty(name="inProcessTime",value = "入站时间")
    @Excel(name = "入站时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "in_process_time")
    private Date inProcessTime;

    /**
     * 出站时间
     */
    @ApiModelProperty(name="outProcessTime",value = "出站时间")
    @Excel(name = "出站时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "out_process_time")
    private Date outProcessTime;

    /**
     * 操作用户ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作用户ID")
    @Excel(name = "操作用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="") 
    @Column(name = "pallet_code")
    private String palletCode;

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

    /**
     * 同包装编码
     */
    @ApiModelProperty(name="samePackageCode",value = "同包装编码")
    @Excel(name = "同包装编码", height = 20, width = 30,orderNum="")
    @Column(name = "same_package_code")
    private String samePackageCode;

    /**
     * 销售编码关联同包装编码ID
     */
    @ApiModelProperty(name="salesCodeReSpcId",value = "销售编码关联同包装编码ID")
    @Excel(name = "销售编码关联同包装编码ID", height = 20, width = 30,orderNum="")
    @Column(name = "sales_code_re_spc_id")
    private Long salesCodeReSpcId;
}