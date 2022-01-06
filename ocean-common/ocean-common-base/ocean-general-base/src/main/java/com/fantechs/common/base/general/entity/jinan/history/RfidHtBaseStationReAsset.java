package com.fantechs.common.base.general.entity.jinan.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * RFID基站资产关系履历表
 * rfid_ht_base_station_re_asset
 * @author admin
 * @date 2021-11-29 10:35:50
 */
@Data
@Table(name = "rfid_ht_base_station_re_asset")
public class RfidHtBaseStationReAsset extends ValidGroup implements Serializable {
    /**
     * 基站资产关系履历ID
     */
    @ApiModelProperty(name="htBaseStationReAssetId",value = "基站资产关系履历ID")
    @Excel(name = "基站资产关系履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_base_station_re_asset_id")
    private Long htBaseStationReAssetId;

    /**
     * 基站资产关系ID
     */
    @ApiModelProperty(name="baseStationReAssetId",value = "基站资产关系ID")
    @Excel(name = "基站资产关系ID", height = 20, width = 30,orderNum="") 
    @Column(name = "base_station_re_asset_id")
    private Long baseStationReAssetId;

    /**
     * 基站ID
     */
    @ApiModelProperty(name="baseStationId",value = "基站ID")
    @Excel(name = "基站ID", height = 20, width = 30,orderNum="") 
    @Column(name = "base_station_id")
    private Long baseStationId;

    /**
     * 资产ID
     */
    @ApiModelProperty(name="assetId",value = "资产ID")
    @Excel(name = "资产ID", height = 20, width = 30,orderNum="") 
    @Column(name = "asset_id")
    private Long assetId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 资产编码
     */
    @Transient
    @ApiModelProperty(name = "assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="8")
    private String assetCode;

    /**
     * 资产名称
     */
    @Transient
    @ApiModelProperty(name = "assetName",value = "资产名称")
    @Excel(name = "资产名称", height = 20, width = 30,orderNum="8")
    private String assetName;

    /**
     * RFID序列号
     */
    @Transient
    @ApiModelProperty(name = "assetBarcode",value = "RFID序列号")
    @Excel(name = "RFID序列号", height = 20, width = 30,orderNum="8")
    private String assetBarcode;

    private static final long serialVersionUID = 1L;
}