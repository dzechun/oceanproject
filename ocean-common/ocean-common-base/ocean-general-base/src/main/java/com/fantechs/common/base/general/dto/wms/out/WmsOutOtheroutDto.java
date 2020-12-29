package com.fantechs.common.base.general.dto.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class WmsOutOtheroutDto extends WmsOutOtherout implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="7")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="9")
    private String modifiedUserName;

    /**
     * 操作人名称
     */
    @Transient
    @ApiModelProperty(name = "operatorName",value = "操作人名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="4")
    private String operatorName;

    private List<WmsOutOtheroutDetDto> wmsOutOtheroutDetDtoList;
}
