package com.fantechs.common.base.general.entity.bcm;

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
@Table(name = "bcm_bar_code")
public class BcmBarCode extends ValidGroup implements Serializable {
    /**
     * 条码\流转卡生产id
     */
    @ApiModelProperty(name="barCodeId",value = "条码/流转卡生产id")
    @Excel(name = "条码/流转卡生产id", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "bar_code_id")
    private Long barCodeId;

    /**
     * 类型:1条码生产，2 流转卡生产
     */
    @ApiModelProperty(name="barCodeType",value = "类型:1条码生产，2 流转卡生产")
    @Excel(name = "类型:1条码生产，2 流转卡生产", height = 20, width = 30,orderNum="") 
    @Column(name = "bar_code_type")
    private Byte barCodeType;

    /**
     * 工单id
     */
    @ApiModelProperty(name="workOrderId",value = "工单id")
    @Excel(name = "工单id", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产品料号绑定标签id
     */
    @ApiModelProperty(name="labelMaterialId",value = "产品料号绑定标签id")
    @Excel(name = "产品料号绑定标签id", height = 20, width = 30,orderNum="") 
    @Column(name = "label_material_id")
    private Long labelMaterialId;

    /**
     * 条码生产数量
     */
    @ApiModelProperty(name="printQuantity",value = "条码生产数量")
    @Excel(name = "条码生产数量", height = 20, width = 30,orderNum="")
    @Column(name = "print_quantity")
    private Integer printQuantity;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}