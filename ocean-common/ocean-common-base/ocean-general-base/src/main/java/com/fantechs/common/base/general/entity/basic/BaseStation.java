package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "base_station")
@Data
public class BaseStation extends ValidGroup implements Serializable {
    private static final long serialVersionUID = -5138905602576927928L;
    /**
     * 工位ID
     */
    @Id
    @Column(name = "station_id")
    @ApiModelProperty(name = "stationId",value = "工位ID")
    @NotNull(groups = update.class,message = "工位id不能为空")
    private Long stationId;

    /**
     * 工位代码
     */
    @Column(name = "station_code")
    @ApiModelProperty(name = "stationCode",value = "工位代码")
    @Excel(name = "工位代码", height = 20, width = 30)
    @NotBlank(message = "工位代码不能为空")
    private String stationCode;

    /**
     * 工位名称
     */
    @Column(name = "station_name")
    @ApiModelProperty(name = "stationName",value = "工位名称")
    @Excel(name = "工位名称", height = 20, width = 30)
    @NotBlank(message = "工位名称不能为空")
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
    @NotNull(message = "工序Id不能为空")
    private Long processId;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    private String processName;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name = "processCode",value = "工序编码")
    @Excel(name = "工序编码", height = 20, width = 30)
    private String processCode;

    /**
     * 工段ID
     */
    @Column(name = "section_id")
    @ApiModelProperty(name = "sectionId",value = "工段ID")
    @NotNull(message = "工段Id不能为空")
    private Long sectionId;

    /**
     * 工段名称
     */
    @Transient
    @ApiModelProperty(name = "sectionName",value = "工段名称")
    @Excel(name = "工段名称", height = 20, width = 30)
    private String sectionName;

    /**
     * 工段编码
     */
    @Transient
    @ApiModelProperty(name = "sectionCode",value = "工段编码")
    @Excel(name = "工段编码", height = 20, width = 30)
    private String sectionCode;

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
    @Excel(name = "状态", height = 20, width = 30,replace = {"无效_0", "有效_1"})
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
