package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class WmsInnerInventoryScrapDto extends WmsInnerInventoryScrap implements Serializable {

    /**
     * 处理人名称
     */
    @ApiModelProperty(name="processorUserName",value = "处理人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum="4")
    private String processorUserName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationId",value = "组织代码")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum="1")
    private String organizationCode;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName",value = "创建人名称")
    private String createUserName;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人名称")
    @Excel(name = "盘存转报废单号", height = 20, width = 30,orderNum="3")
    private String modifiedUserName;
}
