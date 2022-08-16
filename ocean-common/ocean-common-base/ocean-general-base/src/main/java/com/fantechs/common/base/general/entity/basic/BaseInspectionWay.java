package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 检验方式
 * base_inspection_way
 * @author admin
 * @date 2021-05-19 09:24:39
 */
@Data
@Table(name = "base_inspection_way")
public class BaseInspectionWay extends ValidGroup implements Serializable {
    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Id
    @Column(name = "inspection_way_id")
    @NotNull(groups = update.class,message = "检验方式ID不能为空")
    private Long inspectionWayId;

    /**
     * 检验方式编码
     */
    @ApiModelProperty(name="inspectionWayCode",value = "检验方式编码")
    @Excel(name = "检验方式编码", height = 20, width = 30,orderNum="1")
    @Column(name = "inspection_way_code")
    @NotBlank(message = "检验方式编码不能为空")
    private String inspectionWayCode;

    /**
     * 检验方式描述
     */
    @ApiModelProperty(name="inspectionWayDesc",value = "检验方式描述")
    @Excel(name = "检验方式描述", height = 20, width = 30,orderNum="2")
    @Column(name = "inspection_way_desc")
    private String inspectionWayDesc;

    /**
     * 检验类型(1- 来料检验 2- 驻厂检验 3-出货检验 4-Ipqc检验)
     */
    @ApiModelProperty(name="inspectionType",value = "检验类型(1- 来料检验 2- 驻厂检验 3-出货检验 4-Ipqc检验)")
    @Excel(name = "检验类型(1- 来料检验 2- 驻厂检验 3-出货检验 4-Ipqc检验)", height = 20, width = 30,orderNum="3",replace = {"来料检验_1", "驻厂检验_2", "出货检验_3", "Ipqc检验_4"})
    @Column(name = "inspection_type")
    private Byte inspectionType;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="4",replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="7")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="5")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    private static final long serialVersionUID = 1L;
}