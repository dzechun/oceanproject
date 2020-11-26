package com.fantechs.common.base.entity.apply.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2020/11/24
 */
@Data
public class SearchSmtStockDet extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -842599524047420593L;

    /**
     * 备料id
     */
    @ApiModelProperty(name="stockId",value = "备料id")
    @Column(name = "stock_id")
    private Long stockId;
}
