package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDeptImport implements Serializable {

    /**
     * 部门代码
     */
    @ApiModelProperty(name="deptCode" ,value="部门代码")
    @Excel(name = "部门编码(必填)", height = 20, width = 30)
    private String deptCode;

    /**
     * 部门名称
     */
    @ApiModelProperty(name="deptName" ,value="部门名称")
    @Excel(name = "部门名称(必填)", height = 20, width = 30)
    private String deptName;

    /**
     * 部门描述
     */
    @ApiModelProperty(name="deptDesc" ,value="部门描述")
    @Excel(name = "部门描述", height = 20, width = 30)
    private String deptDesc;

    /**
     * 厂别ID
     */
    @ApiModelProperty(name="factoryId" ,value="厂别ID")
    private Long factoryId;

    /**
     * 厂别编码
     */
    @ApiModelProperty(name="factoryCode" ,value="厂别编码")
    @Excel(name = "厂别编码", height = 20, width = 30)
    private String factoryCode;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    private Long parentId;

    /**
     * 上级部门编码
     */
    @ApiModelProperty(name="parentCode",value = "上级部门编码")
    @Excel(name = "上级部门编码", height = 20, width = 30)
    private String parentCode;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(name="status" ,value="状态")
    @Excel(name = "状态", height = 20, width = 30)
    private Integer status;
}
