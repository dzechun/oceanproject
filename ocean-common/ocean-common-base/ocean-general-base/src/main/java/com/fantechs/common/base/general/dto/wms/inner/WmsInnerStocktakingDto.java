package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerStocktakingDto extends WmsInnerStocktaking implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
    private String modifiedUserName;

    /**
     * 线别代码
     */
    @Transient
    @ApiModelProperty(name="proCode" ,value="线别代码")
    @Excel(name = "线别代码", height = 20, width = 30)
    private String proCode;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "线别名称", height = 20, width = 30)
    private String proName;

    /**
     * 线别描述
     */
    @Transient
    @ApiModelProperty(name="proDesc" ,value="线别描述")
    @Excel(name = "线别描述", height = 20, width = 30)
    private String proDesc;

    /**
     * 车间编码
     */
    @Transient
    @Excel(name = "车间编码", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name = "workShopCode",value = "车间编码")
    private String workShopCode;

    /**
     * 车间名称
     */
    @Transient
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name = "workShopName",value = "车间名称")
    private String workShopName;

    /**
     * 车间描述
     */
    @Transient
    @ApiModelProperty(name = "workShopDesc",value = "车间描述")
    @Excel(name = "车间描述", height = 20, width = 30,orderNum="3")
    private String workShopDesc;
}
