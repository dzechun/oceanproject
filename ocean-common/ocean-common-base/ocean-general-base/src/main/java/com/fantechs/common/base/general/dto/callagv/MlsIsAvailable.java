package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MlsIsAvailable {

    @ApiModelProperty(name = "factoryCode", value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(name = "deptCode", value = "部门")
    private String deptCode;

    private List<MlsIsAvailableDetail> list;
}
