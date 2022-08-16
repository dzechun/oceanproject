package com.fantechs.common.base.general.dto.mes.sfc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 产品条码流程更新与写过站记录流程 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProcessDto implements Serializable {

    // 组织ID
    private Long orgId;
    // 工单ID
    private Long workOrderId;
    // 工单编号
    private String workOrderCode;
    // 产品条码
    private String barCode;
    // 客户条码
    private String customerBarcode;
    // 产品条码ID
    private Long workOrderBarcodeId;
    // 设备条码ID
    private Long equipmentBarcodeId;
    //设备编码
    private String equipmentCode;
    //耗时
    private String passTime;
    //成品物料ID
    private Long materialId;
    //半成品物料ID
    private Long partMaterialId;
    // 当前扫码工序ID
    private Long nowProcessId;
    // 当前扫码工位ID
    private Long nowStationId;
    //投产工序ID
    private Long putIntoProcessId;
    //产出工序ID
    private Long outputProcessId;
    // 线别ID
    private Long proLineId;
    // 设备ID（设备ID为空值时写N/A）
    private String equipmentId;
    // 工艺路线ID
    private Long routeId;
    // 作业结果
    private String opResult;
    // 不良现象代码（检验结果是OK时不良现象代码写N/A）
    private String badnessPhenotypeCode;
    // 作业人员ID
    private Long operatorUserId;
    // 过站码
    private String passCode;
    /**
     * 过站码类型：1包箱码，2栈板码
     */
    private byte passCodeType;

}
