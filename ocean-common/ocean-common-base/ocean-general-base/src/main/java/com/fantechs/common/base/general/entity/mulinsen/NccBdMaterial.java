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
 * ncc_bd_material
 * @author 86178
 * @date 2021-12-06 20:58:16
 */
@Data
@Table(name = "ncc_bd_material")
public class NccBdMaterial extends ValidGroup implements Serializable {
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
     * 物料编码
     */
    @ApiModelProperty(name="code",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="") 
    @Column(name = "CODE")
    private String code;

    /**
     * 辅助属性组合类型
     */
    @ApiModelProperty(name="def18",value = "辅助属性组合类型")
    @Excel(name = "辅助属性组合类型", height = 20, width = 30,orderNum="") 
    @Column(name = "DEF18")
    private String def18;

    /**
     * PLM物料编码
     */
    @ApiModelProperty(name="def20",value = "PLM物料编码")
    @Excel(name = "PLM物料编码", height = 20, width = 30,orderNum="") 
    @Column(name = "DEF20")
    private String def20;

    /**
     * 是否删除 （0 未删除 1 已删除）
     */
    @ApiModelProperty(name="dr",value = "是否删除 （0 未删除 1 已删除）")
    @Excel(name = "是否删除 （0 未删除 1 已删除）", height = 20, width = 30,orderNum="") 
    @Column(name = "DR")
    private Integer dr;

    /**
     * 启用状态(1=未启用，2=已启用，3=已停用，)
     */
    @ApiModelProperty(name="enablestate",value = "启用状态(1=未启用，2=已启用，3=已停用，)")
    @Excel(name = "启用状态(1=未启用，2=已启用，3=已停用，)", height = 20, width = 30,orderNum="") 
    @Column(name = "ENABLESTATE")
    private Integer enablestate;

    /**
     * 英语名称
     */
    @ApiModelProperty(name="ename",value = "英语名称")
    @Excel(name = "英语名称", height = 20, width = 30,orderNum="") 
    @Column(name = "ENAME")
    private String ename;

    /**
     * 图号
     */
    @ApiModelProperty(name="graphid",value = "图号")
    @Excel(name = "图号", height = 20, width = 30,orderNum="") 
    @Column(name = "GRAPHID")
    private String graphid;

    /**
     * 助记码
     */
    @ApiModelProperty(name="materialmnecode",value = "助记码")
    @Excel(name = "助记码", height = 20, width = 30,orderNum="") 
    @Column(name = "MATERIALMNECODE")
    private String materialmnecode;

    /**
     * 简称
     */
    @ApiModelProperty(name="materialshortname",value = "简称")
    @Excel(name = "简称", height = 20, width = 30,orderNum="") 
    @Column(name = "MATERIALSHORTNAME")
    private String materialshortname;

    /**
     * 规格型号
     */
    @ApiModelProperty(name="materialtype",value = "规格型号")
    @Excel(name = "规格型号", height = 20, width = 30,orderNum="") 
    @Column(name = "MATERIALTYPE")
    private String materialtype;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="name",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="") 
    @Column(name = "NAME")
    private String name;

    /**
     * 物料分类
     */
    @ApiModelProperty(name="pkMarbasclass",value = "物料分类")
    @Excel(name = "物料分类", height = 20, width = 30,orderNum="") 
    @Column(name = "PK_MARBASCLASS")
    private String pkMarbasclass;

    /**
     * 关联的主单位ID
     */
    @ApiModelProperty(name="pkMeasdoc",value = "关联的主单位ID")
    @Excel(name = "关联的主单位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "PK_MEASDOC")
    private String pkMeasdoc;

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