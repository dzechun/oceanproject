package com.fantechs.common.base.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class SmtSpecItemExcelDTO implements Serializable {

    private static final long serialVersionUID = -3555385426277432870L;
    /**
     * 配置项ID
     */
    @ApiModelProperty(name="specId" ,value="配置项ID")
    private String specId;

    /**
     * 配置项代码
     */
    @ApiModelProperty(name="specCode" ,value="配置项代码")
    @Excel(name = "配置项代码", height = 20, width = 30)
    private String specCode;

    /**
     * 配置项名称
     */
    @ApiModelProperty(name="specName" ,value="配置项名称")
    @Excel(name = "配置项名称", height = 20, width = 30)
    private String specName;

    /**
     * 参数
     */
    @ApiModelProperty(name="para" ,value="参数")
    @Excel(name = "参数", height = 20, width = 30)
    private String para;

    /**
     * 参数值
     */
    @ApiModelProperty(name="paraValue" ,value="参数值")
    @Excel(name = "参数值", height = 20, width = 30)
    private String paraValue;

    /**
     * 创建账号
     */
    @ApiModelProperty(name="createUserId" ,value="创建账号")
    //@Excel(name = "创建账号", height = 20, width = 30)
    private String createUserId;

    /**
     * 创建账号名称
     */
    @Excel(name = "创建账号", height = 20, width = 30)
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号
     */
    @ApiModelProperty(name="modifiedUserId" ,value="修改账号")
    //@Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserId;


    /**
     * 修改账号名称
     */
    @Excel(name = "修改账号", height = 20, width = 30)
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

}
