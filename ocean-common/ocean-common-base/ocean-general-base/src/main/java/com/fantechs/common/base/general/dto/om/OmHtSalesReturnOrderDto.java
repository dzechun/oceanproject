package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class OmHtSalesReturnOrderDto extends OmHtSalesReturnOrder implements Serializable {
    /**
     * 货主
     */
    @Transient
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;

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
