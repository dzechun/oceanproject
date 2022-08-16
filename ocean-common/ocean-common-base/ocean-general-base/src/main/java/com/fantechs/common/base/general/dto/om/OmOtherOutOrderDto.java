package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.Na;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class OmOtherOutOrderDto extends OmOtherOutOrder implements Serializable {
    /**
     * 货主
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    @Excel(name = "货主", height = 20, width = 30,orderNum="3")
    private String materialOwnerName;

    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="4")
    private String supplierName;

    /**
     * 收货人
     */
    @Transient
    @ApiModelProperty(name = "consigneeName",value = "收货人")
    @Excel(name = "收货人", height = 20, width = 30,orderNum="5")
    private String consigneeName;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    @Excel(name = "创建名称", height = 20, width = 30,orderNum="24")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="26")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    @Excel(name = "组织", height = 20, width = 30,orderNum="28")
    private String organizationName;
}
