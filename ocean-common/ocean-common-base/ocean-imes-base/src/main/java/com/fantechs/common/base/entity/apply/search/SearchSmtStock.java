package com.fantechs.common.base.entity.apply.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/11/24
 */
@Data
public class SearchSmtStock extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -4687636312322002226L;

    /**
     * 备料单号
     */
    @ApiModelProperty(name="stockCode",value = "备料单号")
    @Column(name = "stock_code")
    private String stockCode;


    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    private String workOrderCode;
}
