package com.fantechs.common.base.general.entity.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 生产管理-产品栈板表
 * mes_sfc_product_pallet
 * @author bgkun
 * @date 2021-05-08 09:35:04
 */
@Data
@Table(name = "mes_sfc_product_pallet")
public class MesSfcProductPallet extends ValidGroup implements Serializable {
    /**
     * 产品栈板表ID
     */
    @ApiModelProperty(name="productPalletId",value = "产品栈板表ID")
    @Excel(name = "产品栈板表ID", height = 20, width = 30,orderNum="") 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Column(name = "product_pallet_id")
    private Long productPalletId;

    /**
     * 栈板号
     */
    @ApiModelProperty(name="palletCode",value = "栈板号")
    @Excel(name = "栈板号", height = 20, width = 30,orderNum="") 
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 当前包装规格数量
     */
    @ApiModelProperty(name="nowPackageSpecQty",value = "当前包装规格数量")
    @Excel(name = "当前包装规格数量", height = 20, width = 30,orderNum="")
    @Column(name = "now_package_spec_qty")
    private BigDecimal nowPackageSpecQty;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产品ID
     */
    @ApiModelProperty(name="materialId",value = "产品ID")
    @Excel(name = "产品ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工位ID
     */
    @ApiModelProperty(name="stationId",value = "工位ID")
    @Excel(name = "工位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 关闭状态(0-未关闭 1-已关闭)
     */
    @ApiModelProperty(name="closeStatus",value = "关闭状态(0-未关闭 1-已关闭)")
    @Excel(name = "关闭状态(0-未关闭 1-已关闭)", height = 20, width = 30,orderNum="") 
    @Column(name = "close_status")
    private Byte closeStatus;

    /**
     * 关栈板人员ID
     */
    @ApiModelProperty(name="closePalletUserId",value = "关栈板人员ID")
    @Excel(name = "关栈板人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "close_pallet_user_id")
    private Long closePalletUserId;

    /**
     * 关栈板时间
     */
    @ApiModelProperty(name="closePalletTime",value = "关栈板时间")
    @Excel(name = "关栈板时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "close_pallet_time")
    private Date closePalletTime;

    /**
     * 转移状态(0-未转移 1-已转移)
     */
    @ApiModelProperty(name="moveStatus",value = "转移状态(0-未转移 1-已转移)")
    @Excel(name = "转移状态(0-未转移 1-已转移)", height = 20, width = 30,orderNum="")
    @Column(name = "move_status")
    private Byte moveStatus;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}