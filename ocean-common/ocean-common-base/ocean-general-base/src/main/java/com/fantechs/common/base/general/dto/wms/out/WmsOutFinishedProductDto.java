package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 成品出库单返回实体
 * WmsOutFinishedProductDto
 * @author hyc
 * @date 2020-12-22 15:02:48
 */
@Data
public class WmsOutFinishedProductDto extends WmsOutFinishedProduct implements Serializable {

    /**
     * 客户代码
     */
    @ApiModelProperty(name="customerCode" ,value="客户代码")
    private String customerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;


    private static final long serialVersionUID = 1L;
}