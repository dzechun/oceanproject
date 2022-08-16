package com.fantechs.common.base.general.entity.basic;

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
 * base_address
 * @author 53203
 * @date 2020-11-13 17:44:00
 */
@Data
@Table(name = "base_address")
public class BaseAddress extends ValidGroup implements Serializable {
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
    @Column(name = "province_code")
    private String provinceCode;

    /**
     * 市编码
     */
    @ApiModelProperty(name="cityCode",value = "市编码")
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 区/县编码
     */
    @ApiModelProperty(name="classifyCode",value = "区/县编码")
    @Column(name = "classify_code")
    private String classifyCode;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="addressDetail",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30)
    @Column(name = "address_detail")
    @NotBlank(message = "详细地址不能为空")
    private String addressDetail;

    /**
     * 完整地址
     */
    @ApiModelProperty(name="addressDetail",value = "完整地址")
    @Excel(name = "完整地址", height = 20, width = 30,orderNum="1")
    @Column(name = "complete_detail")
    private String completeDetail;

    /**
     * 邮政编码
     */
    @ApiModelProperty(name="postCode",value = "邮政编码")
    @Excel(name = "邮政编码", height = 20, width = 30,orderNum="2")
    @Column(name = "post_code")
    private String postCode;


    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="3",replace = {"无效_0", "有效_1"})
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="4",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
