package com.fantechs.common.base.general.entity.mulinsen;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * ncc_bd_stordoc
 * @author 86178
 * @date 2021-12-06 20:58:17
 */
@Data
@Table(name = "ncc_bd_stordoc")
public class NccBdStordoc extends ValidGroup implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(name="id",value = "主键")
    @Excel(name = "主键", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="code",value = "仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="") 
    @Column(name = "CODE")
    private String code;

    /**
     * 货位管理
     */
    @ApiModelProperty(name="csflag",value = "货位管理")
    @Excel(name = "货位管理", height = 20, width = 30,orderNum="") 
    @Column(name = "CSFLAG")
    private String csflag;

    /**
     * 是否删除 （0 未删除 1 已删除）
     */
    @ApiModelProperty(name="dr",value = "是否删除 （0 未删除 1 已删除）")
    @Excel(name = "是否删除 （0 未删除 1 已删除）", height = 20, width = 30,orderNum="") 
    @Column(name = "DR")
    private Integer dr;

    /**
     * 启用状态（1=未启用，2=已启用，3=已停用）
     */
    @ApiModelProperty(name="enablestate",value = "启用状态（1=未启用，2=已启用，3=已停用）")
    @Excel(name = "启用状态（1=未启用，2=已启用，3=已停用）", height = 20, width = 30,orderNum="") 
    @Column(name = "ENABLESTATE")
    private Integer enablestate;

    /**
     * 废品库
     */
    @ApiModelProperty(name="gubflag",value = "废品库")
    @Excel(name = "废品库", height = 20, width = 30,orderNum="") 
    @Column(name = "GUBFLAG")
    private String gubflag;

    /**
     * 代储仓
     */
    @ApiModelProperty(name="isagentstore",value = "代储仓")
    @Excel(name = "代储仓", height = 20, width = 30,orderNum="") 
    @Column(name = "ISAGENTSTORE")
    private String isagentstore;

    /**
     * 影响可用量
     */
    @ApiModelProperty(name="isatpaffected",value = "影响可用量")
    @Excel(name = "影响可用量", height = 20, width = 30,orderNum="") 
    @Column(name = "ISATPAFFECTED")
    private String isatpaffected;

    /**
     * 进行存货成本计算
     */
    @ApiModelProperty(name="iscalculatedinvcost",value = "进行存货成本计算")
    @Excel(name = "进行存货成本计算", height = 20, width = 30,orderNum="") 
    @Column(name = "ISCALCULATEDINVCOST")
    private String iscalculatedinvcost;

    /**
     * 委外仓
     */
    @ApiModelProperty(name="iscommissionout",value = "委外仓")
    @Excel(name = "委外仓", height = 20, width = 30,orderNum="") 
    @Column(name = "ISCOMMISSIONOUT")
    private String iscommissionout;

    /**
     * 直运仓
     */
    @ApiModelProperty(name="isdirectstore",value = "直运仓")
    @Excel(name = "直运仓", height = 20, width = 30,orderNum="") 
    @Column(name = "ISDIRECTSTORE")
    private String isdirectstore;

    /**
     * 保税仓
     */
    @ApiModelProperty(name="iskptaxstore",value = "保税仓")
    @Excel(name = "保税仓", height = 20, width = 30,orderNum="") 
    @Column(name = "ISKPTAXSTORE")
    private String iskptaxstore;

    /**
     * 可预留
     */
    @ApiModelProperty(name="isobligate",value = "可预留")
    @Excel(name = "可预留", height = 20, width = 30,orderNum="") 
    @Column(name = "ISOBLIGATE")
    private String isobligate;

    /**
     * 适用零售
     */
    @ApiModelProperty(name="isretail",value = "适用零售")
    @Excel(name = "适用零售", height = 20, width = 30,orderNum="") 
    @Column(name = "ISRETAIL")
    private String isretail;

    /**
     * 门店仓库
     */
    @ApiModelProperty(name="isshopstore",value = "门店仓库")
    @Excel(name = "门店仓库", height = 20, width = 30,orderNum="") 
    @Column(name = "ISSHOPSTORE")
    private String isshopstore;

    /**
     * 在途仓
     */
    @ApiModelProperty(name="isstoreontheway",value = "在途仓")
    @Excel(name = "在途仓", height = 20, width = 30,orderNum="") 
    @Column(name = "ISSTOREONTHEWAY")
    private String isstoreontheway;

    /**
     * 备注
     */
    @ApiModelProperty(name="memo",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    @Column(name = "MEMO")
    private String memo;

    /**
     * 计划可用
     */
    @ApiModelProperty(name="mrpflag",value = "计划可用")
    @Excel(name = "计划可用", height = 20, width = 30,orderNum="") 
    @Column(name = "MRPFLAG")
    private String mrpflag;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="name",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="") 
    @Column(name = "NAME")
    private String name;

    /**
     * 加工商
     */
    @ApiModelProperty(name="operatesupplier",value = "加工商")
    @Excel(name = "加工商", height = 20, width = 30,orderNum="") 
    @Column(name = "OPERATESUPPLIER")
    private String operatesupplier;

    /**
     * 电话号码
     */
    @ApiModelProperty(name="phone",value = "电话号码")
    @Excel(name = "电话号码", height = 20, width = 30,orderNum="") 
    @Column(name = "PHONE")
    private String phone;

    /**
     * 所属地点
     */
    @ApiModelProperty(name="pkAddress",value = "所属地点")
    @Excel(name = "所属地点", height = 20, width = 30,orderNum="") 
    @Column(name = "PK_ADDRESS")
    private String pkAddress;

    /**
     * 所属集团
     */
    @ApiModelProperty(name="pkGroup",value = "所属集团")
    @Excel(name = "所属集团", height = 20, width = 30,orderNum="") 
    @Column(name = "PK_GROUP")
    private String pkGroup;

    /**
     * 所属库存组织
     */
    @ApiModelProperty(name="pkOrg",value = "所属库存组织")
    @Excel(name = "所属库存组织", height = 20, width = 30,orderNum="") 
    @Column(name = "PK_ORG")
    private String pkOrg;

    /**
     * 负责人
     */
    @ApiModelProperty(name="principalcode",value = "负责人")
    @Excel(name = "负责人", height = 20, width = 30,orderNum="") 
    @Column(name = "PRINCIPALCODE")
    private String principalcode;

    /**
     * 所属利润中心
     */
    @ApiModelProperty(name="profitcentre",value = "所属利润中心")
    @Excel(name = "所属利润中心", height = 20, width = 30,orderNum="") 
    @Column(name = "PROFITCENTRE")
    private String profitcentre;

    /**
     * 生产仓库
     */
    @ApiModelProperty(name="proflag",value = "生产仓库")
    @Excel(name = "生产仓库", height = 20, width = 30,orderNum="") 
    @Column(name = "PROFLAG")
    private String proflag;

    /**
     * 数据状态：0未同步，1已同步, 2已变更
     */
    @ApiModelProperty(name="dataStatus",value = "数据状态：0未同步，1已同步, 2已变更")
    @Excel(name = "数据状态：0未同步，1已同步, 2已变更", height = 20, width = 30,orderNum="") 
    @Column(name = "DATA_STATUS")
    private Integer dataStatus;

    /**
     * 同步时间
     */
    @ApiModelProperty(name="syncTime",value = "同步时间")
    @Excel(name = "同步时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "SYNC_TIME")
    private Date syncTime;

    private static final long serialVersionUID = 1L;
}