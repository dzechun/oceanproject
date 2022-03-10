package com.fantechs.common.base.general.entity.wms.out.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Data
public class SearchWmsOutDespatchOrder extends BaseQuery implements Serializable {

    @ApiModelProperty("装车单号")
    private String despatchOrderCode;
    @ApiModelProperty("柜号")
    private String containerNumber;
    @ApiModelProperty("封条号")
    private String sealNumber;
    private boolean isPda;

    /**
     * 车牌
     */
    @ApiModelProperty(name="carNumber",value = "车牌")
    private String carNumber;

    /**
     * 司机名字
     */
    @ApiModelProperty(name="driverName",value = "司机名字")
    private String driverName;

    /**
     * 单据状态(1-待装车 2-装车中 3-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待装车 2-装车中 3-完成)")
    private Byte orderStatus;

    private Long orgId;
}
