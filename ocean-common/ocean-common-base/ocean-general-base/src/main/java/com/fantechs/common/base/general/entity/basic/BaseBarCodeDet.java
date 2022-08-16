package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * bcm_bar_code_det
 * @author mr.lei
 * @date 2021-01-22 09:53:53
 */
@Data
@Table(name = "base_bar_code_det")
public class BaseBarCodeDet extends ValidGroup implements Serializable {
    /**
     * 工单条码明细id
     */
    @ApiModelProperty(name="barCodeDetId",value = "工单条码明细id")
    @Excel(name = "工单条码明细id", height = 20, width = 30) 
    @Id
    @Column(name = "bar_code_det_id")
    private Long barCodeDetId;

    /**
     * 工单条码
     */
    @ApiModelProperty(name="barCodeId",value = "工单条码")
    @Excel(name = "工单条码", height = 20, width = 30) 
    @Column(name = "bar_code_id")
    private Long barCodeId;

    /**
     * 条码内容
     */
    @ApiModelProperty(name="barCodeContent",value = "条码内容")
    @Excel(name = "条码内容", height = 20, width = 30) 
    @Column(name = "bar_code_content")
    private String barCodeContent;

    /**
     * 状态(2已入库，1未入库)
     */
    @ApiModelProperty(name="status",value = "状态(2已入库，1未入库)")
    @Excel(name = "状态(2已入库，1未入库)", height = 20, width = 30) 
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30) 
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30) 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30) 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30) 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30) 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30) 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}