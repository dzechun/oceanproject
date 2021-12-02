package com.fantechs.common.base.general.entity.jinan;

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
 * RFID资产管理
 * rfid_asset
 * @author admin
 * @date 2021-11-29 10:35:50
 */
@Data
@Table(name = "rfid_asset")
public class RfidAsset extends ValidGroup implements Serializable {
    /**
     * 资产ID
     */
    @ApiModelProperty(name="assetId",value = "资产ID")
    @Id
    @Column(name = "asset_id")
    @NotNull(groups = update.class,message = "资产ID不能为空")
    private Long assetId;

    /**
     * 资产编码
     */
    @ApiModelProperty(name="assetCode",value = "资产编码")
    @Excel(name = "资产编码", height = 20, width = 30,orderNum="1")
    @Column(name = "asset_code")
    @NotBlank(groups = {add.class,update.class},message = "资产编码不能为空")
    private String assetCode;

    /**
     * 资产名称
     */
    @ApiModelProperty(name="assetName",value = "资产名称")
    @Excel(name = "资产名称", height = 20, width = 30,orderNum="2")
    @Column(name = "asset_name")
    @NotBlank(groups = {add.class,update.class},message = "资产名称不能为空")
    private String assetName;

    /**
     * 资产描述
     */
    @ApiModelProperty(name="assetDesc",value = "资产描述")
    @Excel(name = "资产描述", height = 20, width = 30,orderNum="3")
    @Column(name = "asset_desc")
    private String assetDesc;

    /**
     * 资产条码
     */
    @ApiModelProperty(name="assetBarcode",value = "资产条码")
    @Excel(name = "资产条码", height = 20, width = 30,orderNum="5")
    @Column(name = "asset_barcode")
    private String assetBarcode;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="4")
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


    private static final long serialVersionUID = 1L;
}