package com.fantechs.common.base.general.entity.qms.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SearchQmsInspectionItem extends BaseQuery implements Serializable {

    /**
     * 检验项目单号
     */
    @ApiModelProperty(name="inspectionItemCode",value = "检验项目单号")
    private String inspectionItemCode;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(name="inspectionItemName",value = "检验项目名称")
    private String inspectionItemName;

    /**
     * 检验项目水平
     */
    @ApiModelProperty(name="inspectionItemLevel",value = "检验项目水平")
    private Long inspectionItemLevel;


    /**
     * 检验项
     */
    @ApiModelProperty(name="inspectionNape",value = "检验项")
    private Long inspectionNape;



    private static final long serialVersionUID = 1L;
}
