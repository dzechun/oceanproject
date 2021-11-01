package com.fantechs.common.base.general.entity.kreport;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Warehouse extends ValidGroup implements Serializable {

    @Id
    @ApiModelProperty(name="id",value = "仓库ID")
    private Long id;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="name",value = "仓库名称")
    private String name;


}
