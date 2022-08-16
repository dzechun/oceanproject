package com.fantechs.common.base.general.entity.basic.search;

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
import java.util.Date;


@Data
public class SearchBasePartsInformation extends BaseQuery implements Serializable {
    /**
     * 部件资料ID
     */
    @ApiModelProperty(name="partsInformationId",value = "部件资料ID")
    private Long partsInformationId;

    /**
     * 部件资料编码
     */
    @ApiModelProperty(name="partsInformationCode",value = "部件资料编码")
    private String partsInformationCode;

    /**
     * 部件资料名称
     */
    @ApiModelProperty(name="partsInformationName",value = "部件资料名称")
    private String partsInformationName;


    private static final long serialVersionUID = 1L;
}
