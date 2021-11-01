package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业中单据统计
 */
@Data
public class JobDocumentProgress extends ValidGroup implements Serializable {

    /**
     * 创建日期
     */
    @ApiModelProperty(name="createTime",value = "创建日期")
    private Date createTime;

    /**
     * 拣货单号
     */
    @ApiModelProperty(name="code",value = "拣货单号")
    private String code;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name="shopName",value = "店铺名称")
    private String shopName;

    /**
     * 总数量
     */
    @ApiModelProperty(name="total",value = "总数量")
    private BigDecimal total;

    /**
     * 品项数
     */
    @ApiModelProperty(name="itemNumber",value = "品项数")
    private String itemNumber;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    private String status;

    /**
     * 最后修改人
     */
    @ApiModelProperty(name="lastOperator",value = "最后修改人")
    private String lastOperator;


}
