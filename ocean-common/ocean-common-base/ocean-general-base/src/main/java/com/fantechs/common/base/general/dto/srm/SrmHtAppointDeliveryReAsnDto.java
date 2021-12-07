package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.history.SrmHtAppointDeliveryReAsn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SrmHtAppointDeliveryReAsnDto extends SrmHtAppointDeliveryReAsn implements Serializable {

    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * asn编码
     */
    @ApiModelProperty(name="asnCode",value = "asn编码")
    private String asnCode;


    private static final long serialVersionUID = 1L;
}
