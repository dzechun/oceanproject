package com.fantechs.common.base.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "smt_station")
@Data
public class SmtStation implements Serializable {
    private static final long serialVersionUID = -5138905602576927928L;
    /**
     * 工位ID
     */
    @Id
    @Column(name = "station_id")
    @ApiModelProperty(name = "stationId",value = "工位ID")
    private Long stationId;

    /**
     * 工位代码
     */
    @Column(name = "station_code")
    @ApiModelProperty(name = "stationCode",value = "工位代码")
    @Excel(name = "工位代码", height = 20, width = 30)
    private String stationCode;

    /**
     * 工位名称
     */
    @Column(name = "station_name")
    @ApiModelProperty(name = "stationName",value = "工位名称")
    @Excel(name = "工位名称", height = 20, width = 30)
    private String stationName;

    /**
     * 工位描述
     */
    @Column(name = "station_desc")
    @ApiModelProperty(name = "stationDesc",value = "工位描述")
    @Excel(name = "工位描述", height = 20, width = 30)
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
    @Excel(name = "工序名称", height = 20, width = 30)
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
    @Excel(name = "工段名称", height = 20, width = 30)
    private String sectionName;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name = "status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Integer status;

    /**
     * 是否过站(Y.是  N.否)
     */
    @Column(name = "if_pass_station")
    @ApiModelProperty(name = "ifPassStation",value = "是否过站")
    @Excel(name = "是否过站", height = 20, width = 30)
    private String ifPassStation;

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
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(name = "modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat = "yyyy-MM-dd HH:mm:ss")
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