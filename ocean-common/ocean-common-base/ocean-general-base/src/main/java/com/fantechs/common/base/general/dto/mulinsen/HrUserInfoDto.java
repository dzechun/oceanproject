package com.fantechs.common.base.general.dto.mulinsen;

import com.fantechs.common.base.general.entity.mulinsen.HrUserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HrUserInfoDto extends HrUserInfo {

    @ApiModelProperty(name="deptCode",value = "部门编码")
    private String deptCode;

    @ApiModelProperty(name="erpDeptcode",value = "对应ERP部门编码")
    private String erpDeptcode;
}
