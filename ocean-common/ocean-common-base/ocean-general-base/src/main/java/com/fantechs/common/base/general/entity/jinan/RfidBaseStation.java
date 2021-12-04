package com.fantechs.common.base.general.entity.jinan;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * RFID基站
 * rfid_base_station
 * @author admin
 * @date 2021-11-29 10:35:49
 */
@Data
@Table(name = "rfid_base_station")
public class RfidBaseStation extends ValidGroup implements Serializable {
    /**
     * 基站ID
     */
    @ApiModelProperty(name="baseStationId",value = "基站ID")
    @Id
    @Column(name = "base_station_id")
    @NotNull(groups = update.class,message = "基站ID不能为空")
    private Long baseStationId;

    /**
     * 基站编码
     */
    @ApiModelProperty(name="baseStationCode",value = "基站编码")
    @Excel(name = "基站编码", height = 20, width = 30,orderNum="1",needMerge = true)
    @Column(name = "base_station_code")
    @NotBlank(groups = {add.class,update.class},message = "基站编码不能为空")
    private String baseStationCode;

    /**
     * 基站名称
     */
    @ApiModelProperty(name="baseStationName",value = "基站名称")
    @Excel(name = "基站名称", height = 20, width = 30,orderNum="2",needMerge = true)
    @Column(name = "base_station_name")
    @NotBlank(groups = {add.class,update.class},message = "基站名称不能为空")
    private String baseStationName;

    /**
     * 基站描述
     */
    @ApiModelProperty(name="baseStationDesc",value = "基站描述")
    @Excel(name = "基站描述", height = 20, width = 30,orderNum="3",needMerge = true)
    @Column(name = "base_station_desc")
    private String baseStationDesc;

    /**
     * 基站型号
     */
    @ApiModelProperty(name="baseStationModel",value = "基站型号")
    @Excel(name = "基站型号", height = 20, width = 30,orderNum="4",needMerge = true)
    @Column(name = "base_station_model")
    private String baseStationModel;

    /**
     * 区域ID
     */
    @ApiModelProperty(name="areaId",value = "区域ID")
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 基站IP
     */
    @ApiModelProperty(name="baseStationIp",value = "基站IP")
    @Column(name = "base_station_ip")
    @Excel(name = "基站IP", height = 20, width = 30,orderNum="6",needMerge = true)
    private String baseStationIp;

    /**
     * 基站MAC
     */
    @ApiModelProperty(name="baseStationMac",value = "基站MAC")
    @Column(name = "base_station_mac")
    @Excel(name = "基站MAC", height = 20, width = 30,orderNum="7",needMerge = true)
    @NotBlank(groups = {add.class,update.class},message = "基站MAC不能为空")
    private String baseStationMac;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="8",needMerge = true)
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="9",needMerge = true)
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss",needMerge = true)
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="10",needMerge = true)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="12",needMerge = true)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 区域编码
     */
    @Transient
    @ApiModelProperty(name = "areaCode",value = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @Transient
    @ApiModelProperty(name = "areaName",value = "区域名称")
    @Excel(name = "区域名称", height = 20, width = 30,orderNum="5",needMerge = true)
    private String areaName;

    /**
     * RFID信息
     */
    @ApiModelProperty(name="list",value = "RFID信息")
    @ExcelCollection(name="RFID信息",orderNum="14")
    private List<RfidBaseStationReAsset> list = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}