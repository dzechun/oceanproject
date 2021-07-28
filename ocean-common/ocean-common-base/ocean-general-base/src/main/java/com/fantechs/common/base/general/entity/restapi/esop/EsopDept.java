package com.fantechs.common.base.general.entity.restapi.esop;

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
 * 部门表
 * test_dept
 * @author 81947
 * @date 2021-07-21 17:55:26
 */
@Data
@Table(name = "base_dept")
public class EsopDept extends ValidGroup implements Serializable {
    /**
     * 部门编码
     */
    @ApiModelProperty(name="code",value = "部门编码")
    @Excel(name = "部门编码", height = 20, width = 30,orderNum="") 
    @Id
    private String code;

    /**
     * 部门名称
     */
    @ApiModelProperty(name="name",value = "部门名称")
    @Excel(name = "部门名称", height = 20, width = 30,orderNum="") 
    private String name;

    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 部门层级
     */
    @ApiModelProperty(name="level",value = "部门层级")
    @Excel(name = "部门层级", height = 20, width = 30,orderNum="") 
    private String level;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="") 
    private String status;

    /**
     * 客户类型
     */
    @ApiModelProperty(name="cusType",value = "客户类型")
    @Excel(name = "客户类型", height = 20, width = 30,orderNum="") 
    @Column(name = "cus_type")
    private Integer cusType;

    /**
     * 0-一级分公司，1-二级配件厂
     */
    @ApiModelProperty(name="ifSecond",value = "0-一级分公司，1-二级配件厂")
    @Excel(name = "0-一级分公司，1-二级配件厂", height = 20, width = 30,orderNum="") 
    @Column(name = "if_second")
    private Integer ifSecond;

    /**
     * 是否有下级
     */
    @ApiModelProperty(name="isLeaf",value = "是否有下级")
    @Excel(name = "是否有下级", height = 20, width = 30,orderNum="") 
    @Column(name = "is_leaf")
    private String isLeaf;

    /**
     * 是否制造部门
     */
    @ApiModelProperty(name="isManuFacture",value = "是否制造部门")
    @Excel(name = "是否制造部门", height = 20, width = 30,orderNum="") 
    @Column(name = "is_manu_facture")
    private String isManuFacture;

    @Column(name = "is_proc")
    private String isProc;

    /**
     * 线别ID
     */
    @ApiModelProperty(name="lineTypeId",value = "线别ID")
    @Excel(name = "线别ID", height = 20, width = 30,orderNum="") 
    @Column(name = "line_type_id")
    private String lineTypeId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(name="menuId",value = "菜单ID")
    @Excel(name = "菜单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 备注
     */
    @ApiModelProperty(name="note",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String note;

    /**
     * 组织地址
     */
    @ApiModelProperty(name="organizeAddress",value = "组织地址")
    @Excel(name = "组织地址", height = 20, width = 30,orderNum="") 
    @Column(name = "organize_address")
    private String organizeAddress;

    /**
     * 组织负责人
     */
    @ApiModelProperty(name="organizeMan",value = "组织负责人")
    @Excel(name = "组织负责人", height = 20, width = 30,orderNum="") 
    @Column(name = "organize_man")
    private String organizeMan;

    /**
     * 组织类型
     */
    @ApiModelProperty(name="organizeType",value = "组织类型")
    @Excel(name = "组织类型", height = 20, width = 30,orderNum="") 
    @Column(name = "organize_type")
    private String organizeType;

    /**
     * 标记
     */
    @ApiModelProperty(name="orgFlag",value = "标记")
    @Excel(name = "标记", height = 20, width = 30,orderNum="") 
    @Column(name = "org_flag")
    private String orgFlag;

    @Column(name = "org_type")
    private String orgType;

    /**
     * 支付方式
     */
    @ApiModelProperty(name="payWay",value = "支付方式")
    @Excel(name = "支付方式", height = 20, width = 30,orderNum="") 
    @Column(name = "pay_way")
    private Integer payWay;

    @Column(name = "wc_no")
    private String wcNo;

    @Column(name = "worktime_type")
    private String worktimeType;

    /**
     * 创建人
     */
    @ApiModelProperty(name="createdUser",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="") 
    @Column(name = "created_user")
    private String createdUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createdTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 修改人
     */
    @ApiModelProperty(name="modifyUser",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="") 
    @Column(name = "modify_user")
    private String modifyUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifyTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除
     */
    @ApiModelProperty(name="isDeleted",value = "是否删除")
    @Excel(name = "是否删除", height = 20, width = 30,orderNum="") 
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 分公司所属的事业群编码
     */
    @ApiModelProperty(name="groupCode",value = "分公司所属的事业群编码")
    @Excel(name = "分公司所属的事业群编码", height = 20, width = 30,orderNum="") 
    @Column(name = "group_code")
    private String groupCode;

    /**
     * U9系统组织编码
     */
    @ApiModelProperty(name="u9OrgCode",value = "U9系统组织编码")
    @Excel(name = "U9系统组织编码", height = 20, width = 30,orderNum="") 
    @Column(name = "u9_org_code")
    private String u9OrgCode;

    /**
     * 是否显示
     */
    @ApiModelProperty(name="isWas",value = "是否显示")
    @Excel(name = "是否显示", height = 20, width = 30,orderNum="") 
    @Column(name = "is_was")
    private Byte isWas;

    /**
     * 是否同步实时产能
     */
    @ApiModelProperty(name="isSynRealCapacity",value = "是否同步实时产能")
    @Excel(name = "是否同步实时产能", height = 20, width = 30,orderNum="") 
    @Column(name = "is_syn_real_capacity")
    private Byte isSynRealCapacity;

    /**
     * 二级单位作为供应商时的供应商编码
     */
    @ApiModelProperty(name="suplierCode",value = "二级单位作为供应商时的供应商编码")
    @Excel(name = "二级单位作为供应商时的供应商编码", height = 20, width = 30,orderNum="") 
    @Column(name = "suplier_code")
    private String suplierCode;

    /**
     * 简称
     */
    @ApiModelProperty(name="shortName",value = "简称")
    @Excel(name = "简称", height = 20, width = 30,orderNum="") 
    @Column(name = "short_name")
    private String shortName;

    private static final long serialVersionUID = 1L;
}