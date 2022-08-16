package com.fantechs.common.base.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.security.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SysRoleDto extends SysRole implements Serializable {

}
