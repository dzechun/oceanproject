package com.fantechs.common.base.general.entity.basic.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 物料工序关系履历表
 * base_ht_product_process_re_m
 * @author admin
 * @date 2021-04-28 10:05:50
 */
@Data
@Table(name = "base_ht_product_process_re_m")
public class BaseHtProductProcessReM extends ValidGroup implements Serializable {

    private static final long serialVersionUID = 5123842373635483350L;
    /**
     * 物料工序关系履历表ID
     */
    @ApiModelProperty(name="htProductProcessReMId",value = "物料工序关系履历表ID")
    @Excel(name = "物料工序关系履历表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_product_process_re_m_id")
    private Long htProductProcessReMId;

    /**
     * 物料工序关系表ID
     */
    @ApiModelProperty(name="productProcessReMId",value = "物料工序关系表ID")
    @Excel(name = "物料工序关系表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "product_process_re_m_id")
    private Long productProcessReMId;

    /**
     * 产品ID(即物料ID)
     */
    @ApiModelProperty(name="materialId",value = "产品ID(即物料ID)")
    @Excel(name = "产品ID(即物料ID)", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Excel(name = "工序ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_id")
    private Long processId;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 工序编码
     */
    @Transient
    @ApiModelProperty(name="processCode" ,value="工序编码")
    @Excel(name = "工序编码", height = 20, width = 30)
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30)
    private String processName;

    /**
     * 工序描述
     */
    @Transient
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    @Excel(name = "工序描述", height = 20, width = 30)
    private String processDesc;

    /**
     * 所属工段
     */
    @Transient
    @ApiModelProperty(name="sectionName" ,value="所属工段")
    @Excel(name = "所属工段", height = 20, width = 30)
    private String sectionName;

    /**
     * 工序类别
     */
    @Transient
    @ApiModelProperty(name="processCategoryName" ,value="工序类别")
    @Excel(name = "工序类别", height = 20, width = 30)
    private String processCategoryName;

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    /**
     * 物料清单集合
     */
    private List<BaseHtProductMaterialReP> htList;
}