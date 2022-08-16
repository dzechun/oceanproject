package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Import;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseRouteImport implements Serializable {

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(name="routeCode" ,value="工艺路线代码")
    @Excel(name = "工艺路线编码(必填)", height = 20, width = 30)
    private String routeCode;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称(必填)", height = 20, width = 30)
    private String routeName;

    /**
     * 工艺路线描述
     */
    @ApiModelProperty(name="routeDesc" ,value="工艺路线描述")
    @Excel(name = "工艺路线描述", height = 20, width = 30)
    private String routeDesc;

    /**
     * 工艺路线类型（1、成品 2、半成品 3、部件）
     */
    @ApiModelProperty(name="routeType" ,value="工艺路线类型")
    @Excel(name = "工艺路线类型（1、成品 2、半成品 3、部件）", height = 20, width = 30)
    private Integer routeType;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId" ,value="工序ID")
    private Long processId;

//    /**
//     * 工序编码
//     */
//    @ApiModelProperty(name="processCode" ,value="工序编码")
//    @Excel(name = "工序编码(必填)", height = 20, width = 30)
//    private String processCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称(必填)", height = 20, width = 30)
    private String processName;

    /**
     * 工段ID
     */
    @ApiModelProperty(name="sectionId" ,value="工段ID")
    private Long sectionId;

    /**
     * 工序顺序
     */
    @ApiModelProperty(name="orderNum" ,value="工序顺序")
    @Excel(name = "工序顺序(必填)", height = 20, width = 30)
    private Integer orderNum;

    /**
     * 标准时间
     */
    @ApiModelProperty(name="standardTime" ,value="标准时间")
    @Excel(name = "标准时间", height = 20, width = 30)
    private Integer standardTime;

    /**
     * 准备时间
     */
    @ApiModelProperty(name="readinessTime" ,value="准备时间")
    @Excel(name = "准备时间", height = 20, width = 30)
    private Integer readinessTime;

    /**
     * 是否通过(0.否 1.是)
     */
    @ApiModelProperty(name="isPass" ,value="是否通过(0.否 1.是)")
    @Excel(name = "是否通过(0.否 1.是)", height = 20, width = 30)
    private Integer isPass;

    /**
     * 是否必过工序(0.否/N 1.是/Y)
     */
    @ApiModelProperty(name="isMustPass" ,value="是否必过工序(0.否/N 1.是/Y)")
    @Excel(name = "是否必过工序(0、否 1、是)", height = 20, width = 30)
    private Integer isMustPass;

    /**
     * 检验时间(秒)
     */
    @ApiModelProperty(name="inspectionTime" ,value="检验时间(秒)")
    @Excel(name = "检验时间(秒)", height = 20, width = 30)
    private Integer inspectionTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;
}
