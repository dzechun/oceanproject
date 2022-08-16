package com.fantechs.common.base.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: leifengzhi
 * @Date: 2020/9/14 15:05
 * @Description:
 * @Version: 1.0
 */
@Data
public class SysMenuInListDTO implements Serializable {
    private static final long serialVersionUID = -6753427150030810479L;

    @ApiModelProperty(value = "菜单信息")
    private SysMenuInfoDto sysMenuInfoDto;
    
    @ApiModelProperty(value = "子级菜单")
    private List<SysMenuInListDTO> sysMenuinList;
}
