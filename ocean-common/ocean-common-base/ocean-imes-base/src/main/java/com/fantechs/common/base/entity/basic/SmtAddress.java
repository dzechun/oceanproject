package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 地址信息表
 * smt_address
 * @author 53203
 * @date 2020-11-13 17:44:00
 */
@Data
@Table(name = "smt_address")
public class SmtAddress extends ValidGroup implements Serializable {
    /**
     * 地址ID
     */
    @ApiModelProperty(name="addressId",value = "地址ID")
    @Id
    @Column(name = "address_id")
    @NotNull(groups = update.class,message = "地址id不能为空")
    private Long addressId;

    /**
     * 省编码
     */
    @ApiModelProperty(name="provinceCode",value = "省编码")
    @Excel(name = "省编码", height = 20, width = 30,orderNum="1")
    @Column(name = "province_code")
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(name="cityCode",value = "市编码")
    @Excel(name = "市编码", height = 20, width = 30,orderNum="2")
    @Column(name = "city_code")
    @NotBlank(message = "市编码不能为空")
    private String cityCode;

    /**
     * 区/县编码
     */
    @ApiModelProperty(name="classifyCode",value = "区/县编码")
    @Excel(name = "区/县编码", height = 20, width = 30,orderNum="3")
    @Column(name = "classify_code")
    @NotBlank(message = "区/县编码不能为空")
    private String classifyCode;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="addressDetail",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="4")
    @Column(name = "address_detail")
    @NotBlank(message = "详细地址不能为空")
    private String addressDetail;

    /**
     * 邮政编码
     */
    @ApiModelProperty(name="postCode",value = "邮政编码")
    @Excel(name = "邮政编码", height = 20, width = 30,orderNum="5")
    @Column(name = "post_code")
    @NotBlank(message = "邮政编码不能为空")
    private String postCode;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="7",replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}