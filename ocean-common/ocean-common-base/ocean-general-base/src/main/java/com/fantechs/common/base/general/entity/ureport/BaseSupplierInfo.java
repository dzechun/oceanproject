package com.fantechs.common.base.general.entity.ureport;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class BaseSupplierInfo implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(name = "supplierId",value = "id")
    @Id
    private Long supplierId;

    /**
     *供应商代码
     */
    @ApiModelProperty(name = "supplierCode",value = "供应商代码")
    @Excel(name = "供应商代码", height = 20, width = 30,orderNum="1")
    private String supplierCode;

    /**
     *供应商名称
     */
    @ApiModelProperty(name = "supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="2")
    private String supplierName;

    /**
     *地址
     */
    @ApiModelProperty(name = "addressDetail",value = "地址")
    @Excel(name = "地址", height = 20, width = 30,orderNum="3")
    private String addressDetail;

    /**
     *电话
     */
    @ApiModelProperty(name = "telephone",value = "电话")
    @Excel(name = "电话", height = 20, width = 30,orderNum="4")
    private String telephone;

    /**
     *手机号
     */
    @ApiModelProperty(name = "mobilePhone",value = "手机号")
    @Excel(name = "手机号", height = 20, width = 30,orderNum="5")
    private String  mobilePhone;

    /**
     *邮箱
     */
    @ApiModelProperty(name = "email",value = "邮箱")
    @Excel(name = "邮箱", height = 20, width = 30,orderNum="6")
    private String  email;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="7")
    private String remark;

    /**
     * 创建账号名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="8")
    private String createUserName;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改账号名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
}
