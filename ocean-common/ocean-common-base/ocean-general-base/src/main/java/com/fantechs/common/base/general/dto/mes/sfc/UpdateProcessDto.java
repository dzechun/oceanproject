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

    // 工单ID
    private Long workOrderId;
    // 产品条码
    private String barCode;
    // 当前扫码工序ID
    private Long nowProcessId;
    // 当前扫码工位ID
    private Long nowStationId;
    // 线别ID
    private Long proLineId;
    // 设备ID（设备ID为空值时写N/A）
    private String equipmentId;
    // 工艺路线ID
    private Long routeId;
    // 不良现象代码（检验结果是OK时不良现象代码写N/A）
    private String badnessPhenotypeCode;
    // 作业人员ID
    private Long operatorUserId;

}
