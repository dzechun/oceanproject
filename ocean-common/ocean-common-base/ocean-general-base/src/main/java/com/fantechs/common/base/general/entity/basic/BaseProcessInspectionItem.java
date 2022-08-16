package com.fantechs.common.base.general.entity.basic;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 过程检验项目编码
 * base_process_inspection_item
 * @author admin
 * @date 2021-06-02 10:12:12
 */
@Data
@Table(name = "base_process_inspection_item")
public class BaseProcessInspectionItem extends ValidGroup implements Serializable {

    private static final long serialVersionUID = 7923345606913655603L;
    /**
     * 过程检验项目ID
     */
    @ApiModelProperty(name="processInspectionItemId",value = "过程检验项目ID")
    @Id
    @Column(name = "process_inspection_item_id")
    @NotNull(groups = update.class,message = "过程检验项目ID不能为空")
    private Long processInspectionItemId;

    /**
     * 过程检验项目编码
     */
    @ApiModelProperty(name="processInspectionItemCode",value = "过程检验项目编码")
    @Excel(name = "过程检验项目编码", height = 20, width = 30,orderNum="1")
    @Column(name = "process_inspection_item_code")
    @NotBlank(message = "过程检验项目编码不能为空")
    private String processInspectionItemCode;

    /**
     * 过程检验项目描述
     */
    @ApiModelProperty(name="processInspectionItemDesc",value = "过程检验项目描述")
    @Excel(name = "过程检验项目描述", height = 20, width = 30,orderNum="2")
    @Column(name = "process_inspection_item_desc")
    private String processInspectionItemDesc;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 过程检验项目类型(1-巡检 2-首检 3-首件确认)
     */
    @ApiModelProperty(name="processInspectionItemType",value = "过程检验项目类型(1-巡检 2-首检 3-首件确认)")
    @Excel(name = "过程检验项目类型(1-巡检 2-首检 3-首件确认)", height = 20, width = 30,orderNum="7",replace = {"巡检_1", "首检_2", "首件确认_3"})
    @Column(name = "process_inspection_item_type")
    private Byte processInspectionItemType;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 修改人名称
     */
    @ApiModelProperty(name="modifiedUserName" ,value="修改人名称")
    @Transient
    @Excel(name = "修改人名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name="createUserName" ,value="创建人名称")
    @Transient
    @Excel(name = "创建人名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName" ,value="组织名称")
    @Transient
    private String organizationName;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    @Transient
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="3")
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="materialDesc" ,value="产品描述")
    @Transient
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 产品版本
     */
    @ApiModelProperty(name="materialVersion" ,value="产品版本")
    @Transient
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="5")
    private String materialVersion;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelName" ,value="产品型号")
    @Transient
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="6")
    private String productModelName;

    /**
     * 过程检验项目检验项
     */
    @ApiModelProperty(name="baseProcessInspectionItemItemList" ,value="过程检验项目检验项")
    @Transient
    private List<BaseProcessInspectionItemItem> baseProcessInspectionItemItemList = new ArrayList<>();
}