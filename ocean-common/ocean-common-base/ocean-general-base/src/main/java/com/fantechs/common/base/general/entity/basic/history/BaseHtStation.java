package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "base_ht_station")
@Data
public class BaseHtStation {
    /**
     * 工位历史ID
     */
    @Id
    @Column(name = "ht_station_id")
    @ApiModelProperty(name = "htStationId",value = "工位历史ID")
    private Long htStationId;

    /**
     * 工位ID
     */
    @Column(name = "station_id")
    @ApiModelProperty(name = "stationId",value = "工位ID")
    private Long stationId;

    /**
     * 工位代码
     */
    @Column(name = "station_code")
    @ApiModelProperty(name = "stationCode",value = "工位代码")
    private String stationCode;

    /**
     * 工位名称
     */
    @Column(name = "station_name")
    @ApiModelProperty(name = "stationName",value = "工位名称")
    private String stationName;

    /**
     * 工位描述
     */
    @Column(name = "station_desc")
    @ApiModelProperty(name = "stationDesc",value = "工位描述")
    private String stationDesc;

    /**
     * 工序ID
     */
    @Column(name = "process_id")
    @ApiModelProperty(name = "processId",value = "工序ID")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    private String processName;

    /**
     * 工段ID
     */
    @Column(name = "section_id")
    @ApiModelProperty(name = "sectionId",value = "工段ID")
    private Long sectionId;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "工段名称")
    private String sectionName;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status;

    /**
     * 是否过站(0.否  1.是)
     */
    @Column(name = "if_pass_station")
    @ApiModelProperty(name = "ifPassStation",value = "是否过站")
    @Excel(name = "是否过站", height = 20, width = 30)
    private Integer ifPassStation;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    @ApiModelProperty(name = "createUserId",value = "创建人ID")
    private Long createUserId;

    /**
     * 创建账号名称
     */
    @Transient
    @ApiModelProperty(name="createUserName" ,value="创建账号名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    @ApiModelProperty(name = "modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;

    /**
     * 修改账号名称
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName" ,value="修改账号名称")
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    @ApiModelProperty(name = "isDelete",value = "逻辑删除")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;
}
