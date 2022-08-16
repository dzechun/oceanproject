package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/9/1 10:10
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchBaseDept extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 9111926544708147180L;
    /**
     * 部门代码
     */
    @ApiModelProperty(name="deptCode" ,value="部门代码")
    private String deptCode;

    /**
     * 部门名称
     */
    @ApiModelProperty(name="deptName" ,value="部门名称")
    private String deptName;

    /**
     * 部门描述
     */
    @ApiModelProperty(name="deptDesc" ,value="部门描述")
    private String deptDesc;

    /**
     * 厂别ID
     */
    @ApiModelProperty(name="factoryId" ,value="厂别ID")
    private Long factoryId;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    private Long parentId;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

}
