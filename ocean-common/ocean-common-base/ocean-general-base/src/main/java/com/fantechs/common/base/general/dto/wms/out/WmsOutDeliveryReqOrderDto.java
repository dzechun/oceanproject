package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class WmsOutDeliveryReqOrderDto extends WmsOutDeliveryReqOrder implements Serializable {

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6",needMerge = true)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8",needMerge = true)
    private String modifiedUserName;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="2",needMerge = true)
    private String warehouseName;

    /**
     * 出货通知单明细
     */
    @Transient
    @ApiModelProperty(name = "wmsOutDeliveryReqOrderDetDtos",value = "出货通知单明细")
    @Excel(name = "出货通知单明细", height = 20, width = 30,orderNum="10")
    private List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos = new ArrayList<>();
}
