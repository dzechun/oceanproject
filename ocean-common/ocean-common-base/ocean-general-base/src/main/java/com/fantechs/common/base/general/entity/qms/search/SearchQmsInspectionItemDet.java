package com.fantechs.common.base.general.entity.qms.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SearchQmsInspectionItemDet extends BaseQuery implements Serializable {

    /**
     * 检验项目单号
     */
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    private String inspectionItemCode;

    /**
     * 特殊分类
     */
    @ApiModelProperty(name="specialSort",value = "特殊分类")
    private Long specialSort;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;


    private static final long serialVersionUID = 1L;
}
