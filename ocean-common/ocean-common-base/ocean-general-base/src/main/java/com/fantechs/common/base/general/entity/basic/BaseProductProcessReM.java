package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

/**
 * 物料工序关系表
 * base_product_process_re_m
 * @author admin
 * @date 2021-04-28 10:05:50
 */
@Data
@Table(name = "base_product_process_re_m")
public class BaseProductProcessReM extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 1231695678097063581L;
    /**
     * 物料工序关系表ID
     */
    @ApiModelProperty(name="productProcessReMId",value = "物料工序关系表ID")
    @Id
    @Column(name = "product_process_re_m_id")
    private Long productProcessReMId;

    /**
     * 产品ID(即物料ID)
     */
    @ApiModelProperty(name="materialId",value = "产品ID(即物料ID)")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
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
    @Excel(name = "工序编码", height = 20, width = 30, orderNum="1")
    private String processCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30, orderNum="2")
    private String processName;

    /**
     * 工序描述
     */
    @Transient
    @ApiModelProperty(name="processDesc" ,value="工序描述")
    @Excel(name = "工序描述", height = 20, width = 30, orderNum="3")
    private String processDesc;

    /**
     * 所属工段
     */
    @Transient
    @ApiModelProperty(name="sectionName" ,value="所属工段")
    @Excel(name = "所属工段", height = 20, width = 30, orderNum="4")
    private String sectionName;

    /**
     * 工序类别
     */
    @Transient
    @ApiModelProperty(name="processCategoryName" ,value="工序类别")
    @Excel(name = "工序类别", height = 20, width = 30, orderNum="5")
    private String processCategoryName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30, orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30, orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 物料清单集合
     */
    private List<BaseProductMaterialReP> list = new ArrayList<>();
}