package com.fantechs.common.base.general.entity.basic.history;

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
 * 单价信息历史表
 * base_ht_unit_price
 * @author 53203
 * @date 2021-01-27 16:37:07
 */
@Data
@Table(name = "base_ht_unit_price")
public class BaseHtUnitPrice extends ValidGroup implements Serializable {
    /**
     * 单价信息历史ID
     */
    @ApiModelProperty(name="htUnitPriceId",value = "单价信息历史ID")
    @Excel(name = "单价信息历史ID", height = 20, width = 30)
    @Id
    @Column(name = "ht_unit_price_id")
    private Long htUnitPriceId;

    /**
     * 单价信息ID
     */
    @ApiModelProperty(name="unitPriceId",value = "单价信息ID")
    @Excel(name = "单价信息ID", height = 20, width = 30)
    @Column(name = "unit_price_id")
    private Long unitPriceId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 开料
     */
    @ApiModelProperty(name="cutting",value = "开料")
    @Excel(name = "开料", height = 20, width = 30)
    private BigDecimal cutting;

    /**
     * 封边
     */
    @ApiModelProperty(name="sealEdge",value = "封边")
    @Excel(name = "封边", height = 20, width = 30)
    @Column(name = "seal_edge")
    private BigDecimal sealEdge;

    /**
     * 排钻
     */
    @ApiModelProperty(name="gangDrill",value = "排钻")
    @Excel(name = "排钻", height = 20, width = 30)
    @Column(name = "gang_drill")
    private BigDecimal gangDrill;

    /**
     * 锣机
     */
    @ApiModelProperty(name="gongMachine",value = "锣机")
    @Excel(name = "锣机", height = 20, width = 30)
    @Column(name = "gong_machine")
    private BigDecimal gongMachine;

    /**
     * 机砂
     */
    @ApiModelProperty(name="sandMachine",value = "机砂")
    @Excel(name = "机砂", height = 20, width = 30)
    @Column(name = "sand_machine")
    private BigDecimal sandMachine;

    /**
     * 钉装
     */
    @ApiModelProperty(name="nails",value = "钉装")
    @Excel(name = "钉装", height = 20, width = 30)
    private BigDecimal nails;

    /**
     * 补土
     */
    @ApiModelProperty(name="repairSoil",value = "补土")
    @Excel(name = "补土", height = 20, width = 30)
    @Column(name = "repair_soil")
    private BigDecimal repairSoil;

    /**
     * 胶粒螺母
     */
    @ApiModelProperty(name="glueNut",value = "胶粒螺母")
    @Excel(name = "胶粒螺母", height = 20, width = 30)
    @Column(name = "glue_nut")
    private BigDecimal glueNut;

    /**
     * UV
     */
    @ApiModelProperty(name="uv",value = "UV")
    @Excel(name = "UV", height = 20, width = 30)
    private BigDecimal uv;

    /**
     * 木磨
     */
    @ApiModelProperty(name="woodMill",value = "木磨")
    @Excel(name = "木磨", height = 20, width = 30)
    @Column(name = "wood_mill")
    private BigDecimal woodMill;

    /**
     * 灰磨
     */
    @ApiModelProperty(name="greyMill",value = "灰磨")
    @Excel(name = "灰磨", height = 20, width = 30)
    @Column(name = "grey_mill")
    private BigDecimal greyMill;

    /**
     * 底得保
     */
    @ApiModelProperty(name="bottomGuaranteed",value = "底得保")
    @Excel(name = "底得保", height = 20, width = 30)
    @Column(name = "bottom_guaranteed")
    private BigDecimal bottomGuaranteed;

    /**
     * 底色
     */
    @ApiModelProperty(name="undertone",value = "底色")
    @Excel(name = "底色", height = 20, width = 30)
    private BigDecimal undertone;

    /**
     * 底油
     */
    @ApiModelProperty(name="baseCoat",value = "底油")
    @Excel(name = "底油", height = 20, width = 30)
    @Column(name = "base_coat")
    private BigDecimal baseCoat;

    /**
     * 擦色
     */
    @ApiModelProperty(name="staining",value = "擦色")
    @Excel(name = "擦色", height = 20, width = 30)
    private BigDecimal staining;

    /**
     * 油磨
     */
    @ApiModelProperty(name="oilAbrasion",value = "油磨")
    @Excel(name = "油磨", height = 20, width = 30)
    @Column(name = "oil_abrasion")
    private BigDecimal oilAbrasion;

    /**
     * 干刷效果
     */
    @ApiModelProperty(name="dryBurshing",value = "干刷效果")
    @Excel(name = "干刷效果", height = 20, width = 30)
    @Column(name = "dry_burshing")
    private BigDecimal dryBurshing;

    /**
     * 修黑边
     */
    @ApiModelProperty(name="repairBlackEdge",value = "修黑边")
    @Excel(name = "修黑边", height = 20, width = 30)
    @Column(name = "repair_black_edge")
    private BigDecimal repairBlackEdge;

    /**
     * 面油
     */
    @ApiModelProperty(name="facialOil",value = "面油")
    @Excel(name = "面油", height = 20, width = 30,orderNum="") 
    @Column(name = "facial_oil")
    private BigDecimal facialOil;

    /**
     * 贴报纸
     */
    @ApiModelProperty(name="postNewspaper",value = "贴报纸")
    @Excel(name = "贴报纸", height = 20, width = 30,orderNum="") 
    @Column(name = "post_newspaper")
    private BigDecimal postNewspaper;

    /**
     * 白胚安装
     */
    @ApiModelProperty(name="whiteEmbryoInstall",value = "白胚安装")
    @Excel(name = "白胚安装", height = 20, width = 30,orderNum="") 
    @Column(name = "white_embryo_install")
    private BigDecimal whiteEmbryoInstall;

    /**
     * 装电
     */
    @ApiModelProperty(name="installPower",value = "装电")
    @Excel(name = "装电", height = 20, width = 30,orderNum="") 
    @Column(name = "install_power")
    private BigDecimal installPower;

    /**
     * 成品安装
     */
    @ApiModelProperty(name="finishedProductInstall",value = "成品安装")
    @Excel(name = "成品安装", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_product_install")
    private BigDecimal finishedProductInstall;

    /**
     * 收货
     */
    @ApiModelProperty(name="receipts",value = "收货")
    @Excel(name = "收货", height = 20, width = 30,orderNum="") 
    private BigDecimal receipts;

    /**
     * 配件包
     */
    @ApiModelProperty(name="accessoriesBag",value = "配件包")
    @Excel(name = "配件包", height = 20, width = 30,orderNum="") 
    @Column(name = "accessories_bag")
    private BigDecimal accessoriesBag;

    /**
     * 贴拉手
     */
    @ApiModelProperty(name="pasteShakeHandshandle",value = "贴拉手")
    @Excel(name = "贴拉手", height = 20, width = 30,orderNum="") 
    @Column(name = "paste_shake_handshandle")
    private BigDecimal pasteShakeHandshandle;

    /**
     * 贴镜
     */
    @ApiModelProperty(name="stickMirror",value = "贴镜")
    @Excel(name = "贴镜", height = 20, width = 30,orderNum="") 
    @Column(name = "stick_mirror")
    private BigDecimal stickMirror;

    /**
     * 包装
     */
    @ApiModelProperty(name="pack",value = "包装")
    @Excel(name = "包装", height = 20, width = 30,orderNum="") 
    private BigDecimal pack;

    /**
     * 木条
     */
    @ApiModelProperty(name="woodBars",value = "木条")
    @Excel(name = "木条", height = 20, width = 30,orderNum="") 
    @Column(name = "wood_bars")
    private BigDecimal woodBars;

    /**
     * 木架成品
     */
    @ApiModelProperty(name="woodenProducts",value = "木架成品")
    @Excel(name = "木架成品", height = 20, width = 30,orderNum="") 
    @Column(name = "wooden_products")
    private BigDecimal woodenProducts;

    /**
     * 补货
     */
    @ApiModelProperty(name="replenishment",value = "补货")
    @Excel(name = "补货", height = 20, width = 30,orderNum="") 
    private BigDecimal replenishment;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**c
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     *  产品型号编码
     */
    @Transient
    @ApiModelProperty(name="productModelCode" ,value="产品型号编码")
    @Excel(name = "产品型号", height = 20, width = 30)
    private String productModelCode;

    /**
     *  产品型号名称
     */
    @Transient
    @ApiModelProperty(name="productModelName" ,value="产品型号名称")
    @Excel(name = "产品型号名称", height = 20, width = 30)
    private String productModelName;

    private static final long serialVersionUID = 1L;
}