package com.fantechs.common.base.general.entity.basic.history;

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
 * 部件组成历史表
 * base_ht_plate_parts
 * @author jbb
 * @date 2021-01-15 14:32:50
 */
@Data
@Table(name = "base_ht_plate_parts")
public class BaseHtPlateParts extends ValidGroup implements Serializable {
    /**
     * 部件组成历史ID
     */
    @ApiModelProperty(name="htPlatePartsId",value = "部件组成历史ID")
    @Excel(name = "部件组成历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_plate_parts_id")
    private Long htPlatePartsId;

    /**
     * 部件组成ID
     */
    @ApiModelProperty(name="platePartsId",value = "部件组成ID")
    @Excel(name = "部件组成ID", height = 20, width = 30)
    @Column(name = "plate_parts_id")
    private Long platePartsId;

    /**
     * 是否是定制产品（0、否 1、是）
     */
    @ApiModelProperty(name="ifCustomized",value = "是否是定制产品（0、否 1、是）")
    @Excel(name = "是否是定制产品（0、否 1、是）", height = 20, width = 30)
    @Column(name = "if_customized")
    private Byte ifCustomized;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name="materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum = "1")
    private String materialCode;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name="materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum = "2")
    private String materialName;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc",value = "产品描述")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum = "3")
    private String materialDesc;

    /**
     * 型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelName",value = "型号名称")
    @Excel(name = "型号名称", height = 20, width = 30,orderNum = "4")
    private String productModelName;

    /**
     * 颜色
     */
    @Transient
    @ApiModelProperty(name="color",value = "颜色")
    @Excel(name = "颜色", height = 20, width = 30,orderNum = "5")
    private String color;

    /**
     * 规格编码
     */
    @Transient
    @ApiModelProperty(name="packageSpecificationCode",value = "规格编码")
    @Excel(name = "规格编码", height = 20, width = 30,orderNum = "6")
    private String packageSpecificationCode;

    /**
     * 规格名称
     */
    @Transient
    @ApiModelProperty(name="packageSpecificationName",value = "规格名称")
    @Excel(name = "规格名称", height = 20, width = 30,orderNum = "7")
    private String packageSpecificationName;

    /**
     * 材质
     */
    @Transient
    @ApiModelProperty(name="materialQuality",value = "材质")
    @Excel(name = "材质", height = 20, width = 30,orderNum = "8")
    private String materialQuality;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum = "12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "14")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum = "10")
    private String organizationName;

    private static final long serialVersionUID = 1L;
}
