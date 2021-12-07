package com.fantechs.common.base.dto.security;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SysImportAndExportLogDto extends SysImportAndExportLog implements Serializable {
    private static final long serialVersionUID = 3244097599032499131L;
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="9")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="11")
    private String modifiedUserName;

    /**
     * 操作用户
     */
    @ApiModelProperty(name="operatorUserName",value = "操作用户")
    private Long operatorUserName;

}
