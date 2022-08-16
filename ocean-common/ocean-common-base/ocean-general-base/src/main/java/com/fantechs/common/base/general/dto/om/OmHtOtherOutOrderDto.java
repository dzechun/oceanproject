package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Data
public class OmHtOtherOutOrderDto extends OmHtOtherOutOrder {
    /**
     * 货主
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;

    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    private String supplierName;

    /**
     * 收货人
     */
    @Transient
    @ApiModelProperty(name = "consigneeName",value = "收货人")
    private String consigneeName;

    /**
     * 创建名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建名称")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织")
    private String organizationName;
}
