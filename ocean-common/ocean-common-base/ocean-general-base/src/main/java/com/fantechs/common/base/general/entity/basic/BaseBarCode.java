package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 条码\流转卡生产打印信息
 * bcm_bar_code
 * @author mr.lei
 * @date 2020-12-22 10:27:58
 */
@Data
@Table(name = "base_bar_code")
public class BaseBarCode extends ValidGroup implements Serializable {
    /**
     * 条码\流转卡生产id
     */
    @ApiModelProperty(name="barCodeId",value = "条码/流转卡生产id")
    @Id
    @Column(name = "bar_code_id")
    private Long barCodeId;

    /**
     * 类型:1条码生产，2 流转卡生产
     */
    @ApiModelProperty(name="barCodeType",value = "类型:1条码生产，2 流转卡生产")
    @Column(name = "bar_code_type")
    private Byte barCodeType;

    /**
     * 工单id
     */
    @ApiModelProperty(name="workOrderId",value = "工单id")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产品料号绑定标签id
     */
    @ApiModelProperty(name="labelMaterialId",value = "产品料号绑定标签id")
    @Column(name = "label_material_id")
    private Long labelMaterialId;

    /**
     * 条码生产数量
     */
    @ApiModelProperty(name="printQuantity",value = "条码生产数量")
    @Column(name = "print_quantity")
    private Integer printQuantity;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

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
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}