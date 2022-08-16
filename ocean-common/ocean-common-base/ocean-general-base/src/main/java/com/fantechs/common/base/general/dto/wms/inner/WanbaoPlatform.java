package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

;
;

/**
 * wanbao_platform
 * @author mr.lei
 * @date 2022-06-27 11:41:13
 */
@Data
@Table(name = "wanbao_platform")
public class WanbaoPlatform extends ValidGroup implements Serializable {
    @Id
    @Column(name = "platform_id")
    private Long platformId;

    /**
     * 月台编码
     */
    @ApiModelProperty(name="platformCode",value = "月台编码")
    @Excel(name = "月台编码", height = 20, width = 30,orderNum="") 
    @Column(name = "platform_code")
    private String platformCode;

    /**
     * 月台名称
     */
    @ApiModelProperty(name="platformName",value = "月台名称")
    @Excel(name = "月台名称", height = 20, width = 30,orderNum="") 
    @Column(name = "platform_name")
    private String platformName;

    /**
     * 使用状态(1-空闲 2-使用中)
     */
    @ApiModelProperty(name="usageStatus",value = "使用状态(1-空闲 2-使用中)")
    @Excel(name = "使用状态(1-空闲 2-使用中)", height = 20, width = 30,orderNum="") 
    @Column(name = "usage_status")
    private Byte usageStatus;

    /**
     * 拣货单id
     */
    @ApiModelProperty(name="jobOrderId",value = "拣货单id")
    @Excel(name = "拣货单id", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_id")
    private Long jobOrderId;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 是否校验销售编码(1-是，2-否)
     */
    @ApiModelProperty(name="ifSaleOrder",value = "是否校验销售编码(1-是，2-否)")
    private Byte ifSaleOrder;

    /**
     * 是否校验PO(1-是，2-否)
     */
    @ApiModelProperty(name="ifPo",value = "是否校验PO(1-是，2-否)")
    private Byte ifPo;

    @ApiModelProperty(name = "totalQty",value = "已扫码数量")
    @Transient
    private BigDecimal totalQty;

    private List<WanbaoPlatformDetDto> wanbaoPlatformDetDtos;

    private static final long serialVersionUID = 1L;
}