package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 抽样标准
 * base_sample_standard
 * @author 53203
 * @date 2021-04-06 11:41:15
 */
@Data
@Table(name = "base_sample_standard")
public class BaseSampleStandard extends ValidGroup implements Serializable {
    /**
     * 抽样标准ID
     */
    @ApiModelProperty(name="sampleStandardId",value = "抽样标准ID")
    @Excel(name = "抽样标准ID", height = 20, width = 30)
    @Id
    @Column(name = "sample_standard_id")
    private Long sampleStandardId;

    /**
     * 抽样标准名称
     */
    @ApiModelProperty(name="sampleStandardName",value = "抽样标准名称")
    @Excel(name = "抽样标准名称", height = 20, width = 30,orderNum="1")
    @Column(name = "sample_standard_name")
    private String sampleStandardName;

    /**
     * 抽样标准描述
     */
    @ApiModelProperty(name="sampleStandardDesc",value = "抽样标准描述")
    @Excel(name = "抽样标准描述", height = 20, width = 30,orderNum="2")
    @Column(name = "sample_standard_desc")
    private String sampleStandardDesc;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="3",replace = {"无效_0","有效_1"})
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}