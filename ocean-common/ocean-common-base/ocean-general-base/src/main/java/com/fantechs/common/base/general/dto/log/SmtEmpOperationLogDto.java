package com.fantechs.common.base.general.dto.log;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.log.SmtEmpOperationLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;


@Data
public class SmtEmpOperationLogDto extends SmtEmpOperationLog implements Serializable {

    /**
     * 操作人
     */
    @Transient
    @ApiModelProperty(name = "operationPerson", value = "操作人")
    @Excel(name = "创建用户名称", height = 20, width = 30, orderNum = "13")
    private String operationPerson;

    /**
     * 部门
     */
    @Transient
    @ApiModelProperty(name = "deptName", value = "部门")
    @Excel(name = "部门", height = 20, width = 30, orderNum = "13")
    private String deptName;

    private static final long serialVersionUID = 1L;
}
