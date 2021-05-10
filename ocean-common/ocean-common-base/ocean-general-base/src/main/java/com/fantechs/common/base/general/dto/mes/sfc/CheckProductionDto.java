package com.fantechs.common.base.general.dto.mes.sfc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 过站作业检查record
 *
 * @author dxk
 * @version 1.0
 * @date 2021/04/25
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckProductionDto implements Serializable {

    // 条码，有且只有一条
    private String barCode;
    // 工序ID
    private Long processId;
    // 工位ID
    private Long stationId;
    // 是否检查排程
    private Boolean checkOrNot;

}
