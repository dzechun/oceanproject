package com.fantechs.common.base.general.entity.wanbao;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 读头月台表
 * wanbao_read_head
 * @author bgkun
 * @date 2021-11-29 09:51:35
 */
@Data
@Table(name = "wanbao_read_head")
public class WanbaoReadHead extends ValidGroup implements Serializable {
    /**
     * 读头ID
     */
    @ApiModelProperty(name="readHeadId",value = "读头ID")
    @Id
    @Column(name = "read_head_id")
    private Long readHeadId;

    /**
     * 读头名称
     */
    @ApiModelProperty(name="readHeadName",value = "读头名称")
    @Excel(name = "读头名称", height = 20, width = 30,orderNum="1")
    @Column(name = "read_head_name")
    private String readHeadName;

    /**
     * 读头IP
     */
    @ApiModelProperty(name="readHeadIp",value = "读头IP")
    @Excel(name = "读头IP", height = 20, width = 30,orderNum="2")
    @Column(name = "read_head_ip")
    private String readHeadIp;

    /**
     * 月台ID
     */
    @ApiModelProperty(name="platformId",value = "月台ID")
    @Column(name = "platform_id")
    private Long platformId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="4", replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="5")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}