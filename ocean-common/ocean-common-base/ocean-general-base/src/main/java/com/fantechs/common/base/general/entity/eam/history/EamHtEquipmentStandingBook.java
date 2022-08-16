package com.fantechs.common.base.general.entity.eam.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 设备台账管理履历表
 * eam_ht_equipment_standing_book
 * @author Dylan
 * @date 2021-08-20 14:28:58
 */
@Data
@Table(name = "eam_ht_equipment_standing_book")
public class EamHtEquipmentStandingBook extends ValidGroup implements Serializable {
    /**
     * 设备台账管理履历ID
     */
    @ApiModelProperty(name="htEquipmentStandingBookId",value = "设备台账管理履历ID")
    @Excel(name = "设备台账管理履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_equipment_standing_book_id")
    private Long htEquipmentStandingBookId;

    /**
     * 设备台账管理ID
     */
    @ApiModelProperty(name="equipmentStandingBookId",value = "设备台账管理ID")
    @Excel(name = "设备台账管理ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_standing_book_id")
    private Long equipmentStandingBookId;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    @Excel(name = "设备条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "equipment_barcode_id")
    private Long equipmentBarcodeId;

    /**
     * 财产编码类别(1-固定资产  2-列管品)
     */
    @ApiModelProperty(name="propertyCodeCategory",value = "财产编码类别(1-固定资产  2-列管品)")
    @Excel(name = "财产编码类别(1-固定资产  2-列管品)", height = 20, width = 30,orderNum="") 
    @Column(name = "property_code_category")
    private Byte propertyCodeCategory;

    /**
     * 出厂日期
     */
    @ApiModelProperty(name="releaseDate",value = "出厂日期")
    @Excel(name = "出厂日期", height = 20, width = 30,orderNum="") 
    @Column(name = "release_date")
    private Date releaseDate;

    /**
     * 部门
     */
    @ApiModelProperty(name="deptId",value = "部门")
    @Excel(name = "部门", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 折旧年限
     */
    @ApiModelProperty(name="depreciableLife",value = "折旧年限")
    @Excel(name = "折旧年限", height = 20, width = 30,orderNum="") 
    @Column(name = "depreciable_life")
    private Integer depreciableLife;

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
     * 附件(url集合，用逗号隔开)
     */
    @ApiModelProperty(name="attachmentPath",value = "附件(url集合，用逗号隔开)")
    @Excel(name = "附件(url集合，用逗号隔开)", height = 20, width = 30,orderNum="") 
    @Column(name = "attachment_path")
    private String attachmentPath;

    private static final long serialVersionUID = 1L;
}