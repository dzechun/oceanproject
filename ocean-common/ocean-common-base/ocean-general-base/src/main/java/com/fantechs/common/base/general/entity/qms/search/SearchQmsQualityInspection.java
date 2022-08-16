package com.fantechs.common.base.general.entity.qms.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/12/16 11:32
 */
@Data
public class SearchQmsQualityInspection extends BaseQuery implements Serializable {

    /**
     * 质检单号
     */
    @ApiModelProperty(name="qualityInspectionCode",value = "质检单号")
    private String qualityInspectionCode;

    /**
     * 单据状态（0-未检验 1-检验中 2-已检验）
     */
    @ApiModelProperty(name="billsStatus",value = "单据状态（0-未检验 1-检验中 2-已检验）")
    private Byte billsStatus;

    /**
     * 单据类型
     */
    @ApiModelProperty(name="billsType",value = "单据类型")
    private Long billsType;

    /**
     * 检验区域
     */
    @ApiModelProperty(name="examinationRegion",value = "检验区域")
    private String examinationRegion;

}
