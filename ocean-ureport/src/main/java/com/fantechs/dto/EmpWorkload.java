package com.fantechs.dto;

import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Date 2021/3/8 18:30
 */
@Data
public class EmpWorkload extends ValidGroup implements Serializable {

    @Id
    private Long id;

    /**
     * 员工号
     */
    @ApiModelProperty(name = "empNumber", value = "员工号")
    private String empNumber;

    /**
     * 姓名
     */
    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    /**
     * 单据号
     */
    @ApiModelProperty(name = "documentCode", value = "单据号")
    private String documentCode;

    /**
     * 单据类型
     */
    @ApiModelProperty(name = "documentType", value = "单据类型")
    private String documentType;

    /**
     * 业务日期
     */
    @ApiModelProperty(name = "businessDate", value = "业务日期")
    private Date businessDate;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 对象
     */
    @ApiModelProperty(name = "object", value = "对象")
    private Object object;

}
