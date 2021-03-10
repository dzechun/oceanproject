package com.fantechs.common.base.general.dto.mes.pm.history;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtOrderMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class MesHtOrderMaterialDTO extends MesHtOrderMaterial implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;

    /**
     * 包装方式
     */
    @ApiModelProperty(value = "包装方式",example = "包装方式")
    @Column(name = "packing_unit_name")
    @Excel(name = "包装方式")
    private String packingUnitName;
}