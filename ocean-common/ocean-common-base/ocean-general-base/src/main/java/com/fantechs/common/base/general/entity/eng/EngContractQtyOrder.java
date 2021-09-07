package com.fantechs.common.base.general.entity.eng;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 合同量单
 * eng_contract_qty_order
 * @author Dylan
 * @date 2021-09-01 17:34:57
 */
@Data
@Table(name = "eng_contract_qty_order")
public class EngContractQtyOrder extends ValidGroup implements Serializable {
    /**
     * 合同量单ID
     */
    @ApiModelProperty(name="contractQtyOrderId",value = "合同量单ID")
    @Excel(name = "合同量单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "contract_qty_order_id")
    private Long contractQtyOrderId;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="") 
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30,orderNum="") 
    @Column(name = "device_code")
    private String deviceCode;

    /**
     * 主项号
     */
    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="") 
    @Column(name = "dominant_term_code")
    private String dominantTermCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="") 
    @Column(name = "location_num")
    private String locationNum;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="") 
    @Column(name = "material_code")
    private String materialCode;

    /**
     * 材料用途
     */
    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    @Excel(name = "材料用途", height = 20, width = 30,orderNum="") 
    @Column(name = "material_purpose")
    private String materialPurpose;

    /**
     * 采购数
     */
    @ApiModelProperty(name="purQty",value = "采购数")
    @Excel(name = "采购数", height = 20, width = 30,orderNum="") 
    @Column(name = "pur_qty")
    private String purQty;

    /**
     * 专业编码
     */
    @ApiModelProperty(name="professionCode",value = "专业编码")
    @Excel(name = "专业编码", height = 20, width = 30,orderNum="") 
    @Column(name = "profession_code")
    private String professionCode;

    /**
     * 专业名称
     */
    @ApiModelProperty(name="professionName",value = "专业名称")
    @Excel(name = "专业名称", height = 20, width = 30,orderNum="")
    @Column(name = "profession_name")
    private String professionName;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;


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

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * option1
     */
    @ApiModelProperty(name="option1",value = "option1")
    @Excel(name = "option1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * option2
     */
    @ApiModelProperty(name="option2",value = "option2")
    @Excel(name = "option2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * option3
     */
    @ApiModelProperty(name="option3",value = "option3")
    @Excel(name = "option3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}